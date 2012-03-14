import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.map.LazyMap;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;

/**
 * Retrieves all the unique http links in the last 100 most recent tweets using the supplied hashtag
 *
 * @author Geeta Boppana
 */
public final class GetTweets {
    /**
     * Usage: java com.twitter.utils.GetTweets [hashtag]
     *
     * @param args hashtag
     */
    public static void main(String[] args) {
            	if (args.length < 1) {
            System.out.println("Usage: java com.twitter.utils.GetTweets [hashtag]");
            System.exit(-1);
        }
            	System.out.println("Retrieves all the unique http links in the last "+			
			"100 most recent tweets using hashtag [ " + args[0] + " ].");
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            String qrySearch = args[0]; 
            final int RESULT_SET_SIZE = 100; 
            Map tweetURLs = getListMap();
            Query query = new Query(qrySearch); 
            query.setRpp(RESULT_SET_SIZE);
            List list = null;
            QueryResult result = twitter.search(query);           
            System.out.println("Result Set Size: " + result.getTweets().size()); 
            for (Tweet tweet : result.getTweets()) { 
            	//System.out.println(tweet.getText()); 
            	URLEntity[] urlEntities = tweet.getURLEntities();
            	for(URLEntity urlEntity:urlEntities) {            		
            		list = (List) tweetURLs.get(urlEntity.getURL());
            		list.add(urlEntity.getURL());
            		if(list.size() > 1) {
            			System.out.println("duplicate url");
            		} else {
                		System.out.println("Display URL "+ urlEntity.getDisplayURL());
            			System.out.println("Internal URL "+ urlEntity.getURL());

            		}
            	}            	   
            } 
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get tweets: " + te.getMessage());
            System.exit(-1);
        }
    }
        private static Map getListMap() {
    	return LazyMap.decorate(new HashMap(), new Factory() {
    		public Object create() {
    			return new ArrayList();
    		}
    	});
    }
}
 