package edu.uci.ics.githubuserskills.dataAccess;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

import com.mongodb.BasicDBList;

import edu.uci.ics.githubuserskills.model.Comments;
import edu.uci.ics.githubuserskills.model.Commit;
import edu.uci.ics.githubuserskills.model.RawSkillData;
import edu.uci.ics.githubuserskills.model.SkillDataType;

/**
 * @author shriti
 * Class to bring together all data access objects to produce basic unit of 
 * information for Lucene as <RawSkillData>. 
 *
 */
public class DataAggregator {

	/**
	 * @param login
	 * @return ArrayList<RawSkillData>
	 * @throws UnknownHostException
	 */
	public ArrayList<RawSkillData> getAuthorData(String login) throws UnknownHostException
	{
		ArrayList<RawSkillData> rawDataList = new ArrayList<RawSkillData>();
		ContentDAO contentDAO = new ContentDAO();
		//get commit data
		ArrayList<Commit> authorCommits =  (ArrayList<Commit>) contentDAO.getCommits(login);
		if(authorCommits.size()!=0)
		{
			Iterator<Commit> it = authorCommits.iterator();
			while(it.hasNext())
			{
				Commit commit = it.next();
				RawSkillData commitPatchData = new RawSkillData();
				RawSkillData commitMessageData = new RawSkillData();
				commitPatchData.setAuthor(login);
				commitMessageData.setAuthor(login);
				commitPatchData.setType(SkillDataType.COMMIT_PATCH);
				commitMessageData.setType(SkillDataType.COMMIT_MESSAGE);
				commitPatchData.setTimestamp(Timestamp.valueOf(commit.getTime().substring(0,10) + " 00:00:00").getTime());
				commitMessageData.setTimestamp(Timestamp.valueOf(commit.getTime().substring(0,10) + " 00:00:00").getTime());

				commitPatchData.setContents(commit.getPatch());
				commitMessageData.setContents(commit.getCommit_message());
				rawDataList.add(commitPatchData);
				rawDataList.add(commitMessageData);
			}	
		}

		//get issue comments
		ArrayList<Comments> authorIssueComments =  (ArrayList<Comments>) contentDAO.getIssueComments(login);
		if(authorIssueComments.size()!=0)
		{
			Iterator<Comments> it = authorIssueComments.iterator();
			while(it.hasNext())
			{
				Comments comment = it.next();
				RawSkillData authorComment = new RawSkillData();
				authorComment.setAuthor(login);
				authorComment.setType(SkillDataType.ISSUE_COMMENT);
				authorComment.setTimestamp(Timestamp.valueOf(comment.getTime().substring(0,10) + " 00:00:00").getTime());
				authorComment.setContents(comment.getComment());
				rawDataList.add(authorComment);

			}
		}

		//get pull request comments
		ArrayList<Comments> authorPullRequestComments =  (ArrayList<Comments>) contentDAO.getPullRequestComments(login);
		if(authorPullRequestComments.size()!=0)
		{
			Iterator<Comments> it = authorPullRequestComments.iterator();
			while(it.hasNext())
			{
				Comments comment = it.next();
				RawSkillData authorPullRequestComment = new RawSkillData();
				authorPullRequestComment.setAuthor(login);
				authorPullRequestComment.setType(SkillDataType.PULL_REQUEST_COMMENT);
				authorPullRequestComment.setTimestamp(Timestamp.valueOf(comment.getTime().substring(0,10) + " 00:00:00").getTime());
				authorPullRequestComment.setContents(comment.getComment());
				rawDataList.add(authorPullRequestComment);

			}
		}

		//get commit comments
		ArrayList<Comments> authorCommitComments =  (ArrayList<Comments>) contentDAO.getCommitComments(login);
		if(authorCommitComments.size()!=0)
		{
			Iterator<Comments> it = authorCommitComments.iterator();
			while(it.hasNext())
			{
				Comments comment = it.next();
				RawSkillData authorCommitComment = new RawSkillData();
				authorCommitComment.setAuthor(login);
				authorCommitComment.setType(SkillDataType.COMMIT_COMMENT);
				authorCommitComment.setTimestamp(Timestamp.valueOf(comment.getTime().substring(0,10) + " 00:00:00").getTime());
				authorCommitComment.setContents(comment.getComment());
				rawDataList.add(authorCommitComment);

			}
		}
		
		return rawDataList;

	}

	/**
	 * @param login
	 * @return HashMap<String, ArrayList<RawSkillData>>
	 * @throws UnknownHostException
	 */
	public ArrayList<RawSkillData> getAllLogins() throws UnknownHostException
	{
		ArrayList<RawSkillData> rawDataList = new ArrayList<RawSkillData>();
		AuthorAndUserDAO dao = new AuthorAndUserDAO();
		BasicDBList logins = dao.getAllLogins();
		Iterator<Object> it = logins.iterator();
		while(it.hasNext())
		{
			Object s = it.next();
			rawDataList.addAll(getAuthorData(s.toString()));
		}

		return rawDataList;
	}
}

