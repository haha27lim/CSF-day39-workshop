package sg.edu.nus.iss.app.server.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.app.server.model.Comment;

@Repository
public class CharacterRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COMMENTS_COL = "comments";

    // insert a comment into the database
    public Comment insertComment(Comment r) {
        return mongoTemplate.insert(r, COMMENTS_COL);
    }

    // get all comments for a given character ID
    public List<Comment> getAllComments(String charId){
        // set up pagination for comments
        Pageable pageable = PageRequest.of(0, 10);

        // create query to find comments for a given character ID
        Query patientsDynamicQuery = new Query()
                    .addCriteria(Criteria.where("charId").is(charId))
                    .with(pageable);

        // find comments that match the query
        List<Comment> filteredPatients = 
        mongoTemplate.find(patientsDynamicQuery, Comment.class, COMMENTS_COL);

        // create a pageable comment list
        Page<Comment> patientPage = PageableExecutionUtils.getPage(
                filteredPatients,
                pageable,
                () -> mongoTemplate.count(patientsDynamicQuery, Comment.class));

        return patientPage.toList(); // return the list of comments
    }
}
