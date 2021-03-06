package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UserTest {
    User questioner;
    User respondent;
    Board board;
    Question question;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        board = new Board("Test Board");
        questioner = new User(board, "Test Questioner");
        question = questioner.askQuestion("What is a test?");
        respondent = new User(board, "Test Respondent");
    }

    @Test
    public void questionReputationGoesUpWhenUpVoted() throws Exception {
        int reputationBeforeUpVote = questioner.getReputation();

        respondent.upVote(question);

        assertEquals(questioner.getReputation(), reputationBeforeUpVote + 5);
    }

    @Test
    public void respondentReputationGoesUpWhenUpVoted() throws Exception {
        Answer answer = respondent.answerQuestion(question, "Test Answer!");
        int reputationBeforeUpVote = respondent.getReputation();

        questioner.upVote(answer);

        assertEquals(respondent.getReputation(), reputationBeforeUpVote + 10);
    }

    @Test
    public void respondentReputationGoesDownWhenDownVoted() throws Exception {
        Answer answer = respondent.answerQuestion(question, "Wrong answer");
        int reputationBeforeDownVote = respondent.getReputation();

        questioner.downVote(answer);

        assertEquals(respondent.getReputation(), reputationBeforeDownVote - 1);
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

    @Test
    public void onlyOriginalQuestionerCanAcceptAnswer() throws Exception {
        Answer answer = respondent.answerQuestion(question, "Test Answer");
        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage(is("Only Test Questioner can accept this answer as it is their question"));

        respondent.acceptAnswer(answer);
    }
}