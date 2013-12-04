package edu.uci.ics.githubuserskills.dataAccess;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import edu.uci.ics.githubuserskills.model.Comments;
import edu.uci.ics.githubuserskills.model.Commit;
import edu.uci.ics.githubuserskills.model.RawSkillData;

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
				commitPatchData.setType("patch");
				commitMessageData.setType("message");
				commitPatchData.setTimestamp(Timestamp.parse(commit.getTime()));
				commitMessageData.setTimestamp(Timestamp.parse(commit.getTime()));

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
				authorComment.setType("issue_comment");
				authorComment.setTimestamp(Timestamp.parse(comment.getTime()));
				authorComment.setContents(comment.getComment());
				rawDataList.add(authorComment);

			}
		}

		//get pull request comments
		ArrayList<Comments> authorPullRequestComments =  (ArrayList<Comments>) contentDAO.getIssueComments(login);
		if(authorPullRequestComments.size()!=0)
		{
			Iterator<Comments> it = authorPullRequestComments.iterator();
			while(it.hasNext())
			{
				Comments comment = it.next();
				RawSkillData authorPullRequestComment = new RawSkillData();
				authorPullRequestComment.setAuthor(login);
				authorPullRequestComment.setType("issue_comment");
				authorPullRequestComment.setTimestamp(Timestamp.parse(comment.getTime()));
				authorPullRequestComment.setContents(comment.getComment());
				rawDataList.add(authorPullRequestComment);

			}
		}

		//get commit comments
		//TODO: implement/call method from ContentDAO
		return rawDataList;

	}

	/**
	 * @param login
	 * @return HashMap<String, ArrayList<RawSkillData>>
	 * @throws UnknownHostException
	 */
	public HashMap<String, ArrayList<RawSkillData>> getDataMap() throws UnknownHostException
	{
		HashMap<String, ArrayList<RawSkillData>> dataMap = new HashMap<String, ArrayList<RawSkillData>>();
		AuthorAndUserDAO dao = new AuthorAndUserDAO();
		ArrayList<String> logins = (ArrayList<String>) dao.getAllLogins();
		Iterator<String> it = logins.iterator();
		while(it.hasNext())
		{
			String s = it.next();
			dataMap.put(s, getAuthorData(s));
		}

		return dataMap;
	}
}
