package utility;

import net.rcarz.jiraclient.*;
import net.rcarz.jiraclient.Issue.FluentCreate;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;

import java.io.File;

public class JiraServiceProvider {
	public JiraClient jira;
	public String project;
	public JiraServiceProvider(String jiraUrl, String username, String password, String project) {
		try {
			BasicCredentials creds = new BasicCredentials(username, password);
			jira = new JiraClient(jiraUrl, creds);
			this.project = project;
		} catch (Exception e) {
		}
	}
	public  void createJiraTicket(String issueType, String summary, String description) {

		try {
			FluentCreate fleuntCreate = jira.createIssue(project, issueType);
			fleuntCreate.field(Field.SUMMARY, summary);
			fleuntCreate.field(Field.DESCRIPTION, description);
//			fleuntCreate.field(Field.ATTACHMENT,new FileBody());
			MultipartEntityBuilder entity = MultipartEntityBuilder.create();
			entity.addPart("file",new FileBody(new File(".\\ScreenShots\\OR.txt")));
			Issue newIssue = fleuntCreate.execute();
			System.out.println("new issue created in jira with ID: " + newIssue);

		}
		catch (JiraException e) {
			e.printStackTrace();
		}

	}

}
