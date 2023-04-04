import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class WebShareService {
  webNavigator: any = null; // stores the web navigator object

  constructor() {
    this.webNavigator = window.navigator;
  }

  // check if web share API is supported
  canShare(): boolean {
    // check if web navigator object exists and has a share() method
    return this.webNavigator !== null && this.webNavigator.share !== undefined;
  }

  // check if sharing files is supported
  canShareFile(file: []): boolean {
    // check if web navigator object exists, has a share() method, and can share files
    return this.webNavigator !== null && this.webNavigator.share !== undefined && this.webNavigator.canShare({ files: file });
  }

  // share content using web share API
  share({ title, text, url, files }: { title: string, text?: string, url?: string, files?: any[] }) {
    // return a promise for sharing content
    return new Promise(async (resolve, reject) => {
      // check if web share API is supported
      if (this.webNavigator !== null && this.webNavigator.share !== undefined) {
        if (
          (text === undefined || text === null) &&
          (url === undefined || url === null)
        ) {
          console.warn(`text and url both can't be empty, at least provide either text or url`);
        } else {
          try {
            // create share object with provided content
            const shareObject: ShareObject = {
                title,
                text,
                url
            };
            // add files to share object if provided
            if (files && files.length !== 0) {
                shareObject.files = files;
            }
            // call web share API with share object
            const isShared = await this.webNavigator.share(shareObject);
            resolve({
              shared: true // resolve promise if share is successful
            });
          } catch (error) {
            reject({
              shared: false,
              error // reject promise if share fails
            });
          }
        }
      } else {
        reject({
          shared: false,
          error: `This service/api is not supported in your Browser` // reject promise if web share API is not supported
        });
      }
    });
  }
}

// define interface for share object
interface ShareObject {
  title: string;
  text?: string;
  url?: string;
  files?: any[];
}