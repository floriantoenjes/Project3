package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    User questioner;
    User respondent;
    Board board;
    Question question;


    @Before
    public void setUp() throws Exception {
        board = new Board("Test Board");
        questioner = new User(board, "Questioner");
        question = questioner.askQuestion("What is a test?");
        respondent = new User(board, "Respondent");
    }

    @Test
    public void questionReputationGoesUpWhenUpvoted() throws Exception {
        int reputationBefore = questioner.getReputation();

        respondent.upVote(question);

        assertEquals(questioner.getReputation(), reputationBefore + 5);
    }

    @Test
    public void answerReputationGoesUpWhenUpvoted() throws Exception {
        Answer answer = respondent.answerQuestion(question, "Test Answer!");
        int reputationBefore = respondent.getReputation();

        questioner.upVote(answer);

        assertEquals(respondent.getReputation(), reputationBefore + 10);
    }

    @Test
    public void havingAnswerAcceptedGivesReputationBoost() throws Exception {
        Answer answer = respondent.answerQuestion(question, "Test Answer");
        int reputationBefore = respondent.getReputation();

        questioner.acceptAnswer(answer);

        assertEquals(respondent.getReputation(), reputationBefore + 15);
    }

    @Test (expected = VotingException.class)
    public void upVotingOwnQuestionIsNotAllowed() throws Exception {
        questioner.upVote(question);
    }

    @Test (expected = VotingException.class)
    public void upVotingOwnAnswerIsNotAllowed() throws Exception {
        Answer answer = respondent.answerQuestion(question, "Test Answer");

        respondent.upVote(answer);
    }

    @Test (expected = VotingException.class)
    public void downVotingOwnQuestionIsNotAllowed() throws Exception {
        questioner.downVote(question);
    }

    @Test (expected = VotingException.class)
    public void downVotingOwnAnswerIsNotAllowed() throws Exception {
        Answer answer = respondent.answerQuestion(question, "Test Answer");

        respondent.downVote(answer);
    }
}