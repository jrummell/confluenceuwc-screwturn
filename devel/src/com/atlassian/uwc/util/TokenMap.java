package com.atlassian.uwc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * This is a helper class to create, store and retrieve tokens.
 * <p/>
 * Certain elements such as links and code  can be quite tricky
 * to convert. One issue is that you need to escape text in some places
 * but not others (like inside links).
 * <p/>
 * Use this class for anything where you want to avoid syntaxt from
 * being escaped. VERY HELPFUL.
 */
public class TokenMap {
    protected static Logger log = Logger.getLogger("TokenMap");
    public final static String TOKEN_START = "~UWC_TOKEN_START~";
    public final static String TOKEN_END = "~UWC_TOKEN_END~";

    private static HashMap<String, String> tokenCache = new HashMap<String, String>();
    private static Stack<String> keyStack = new Stack<String>();
    private static long tokenCounter = (new Date()).getTime();

    public synchronized static String add(String textToReplaceWithToken) {
        // assemble token
        tokenCounter++;
        String keyToken = TOKEN_START + tokenCounter + TOKEN_END;
        // add to Map
        if (tokenCache.get(tokenCounter) != null) {
            log.error("DUPLICATE TOKEN!  " + tokenCounter);
            throw new Error("DUPLICATE TOKEN!");
        }
//        log.debug("tokenizing: " + keyToken + ", " + textToReplaceWithToken); //COMMENT
        tokenCache.put(keyToken, textToReplaceWithToken);
        keyStack.push(keyToken);
        return keyToken;
    }

    /**
     * retrieves a value from the map, but uplon retrieving also
     * removes the value
     *
     * @param token
     * @return original value
     */
    public synchronized static String getValueAndRemove(String token) {
        String value = tokenCache.get(token);
        tokenCache.remove(token);
        return value;
    }

    /**
     * replaces all the tokens in the input string with the values
     * stored in the cache and then removes them from the cache to
     * keep it lean
     *
     * @param inputText
     * @return detokenized text
     */
    public synchronized static String detokenizeText(String inputText) {
        String result = inputText;
        Stack<String> keys = getKeys();
        Collection<String> keysToRemove = new ArrayList();
        int iteration = 1;
        int previousTokenCacheSize = tokenCache.size();
        // sometimes tokens get tokenized in which case we have to keep unrolling, hence this while loop
        while (tokenCache.size() > 0) {
        	String key = null;
            while (!keys.empty()) { 
            	key = keys.pop(); //We use a stack so that the detokenizing order is properly maintained UWC-398
//            	log.debug("key = " + key); //COMMENT
                // if the key/token is found in the input replace it with the original value, 
            	// remove from the cache and iterate
                if (result.contains(key)) {
                    String value = tokenCache.get(key);
//                log.debug("detokenizing key = "+key+"  value= "+value); //COMMENT
                    result = result.replace(key, value);
                } else {
                    log.error("key not found for value: " + tokenCache.get(key));
                }
                keysToRemove.add(key); //moved this here because we sometims get some sort of race that causes a loop.
            }
            // clean up the cache by removing the keys that have
            // already been used. these are unique and won't be needed further
            for (String keyToRemove : keysToRemove) {
                tokenCache.remove(keyToRemove);
            }
//            log.debug("detokenizing iteration " + iteration++ + "  tokenCache size = " + tokenCache.size()); //COMMENT
            // a bit arbitrary, but break out of the loop if we can't seem to get the tokens out
            if (previousTokenCacheSize==tokenCache.size() && iteration>10) {
                log.info("breaking out of detokenizing loop: cache size = "+previousTokenCacheSize+"  cache = "+tokenCache);
                log.info("text = "+result); //COMMENT
                tokenCache.clear();
                keyStack.clear();
                break;
            }
            previousTokenCacheSize = tokenCache.size();
        }
        return result;
    }

	private synchronized static Stack<String> getKeys() {
		return keyStack;
	}

    /**
     * calls replaceAndTokenize with no flags
     *
     * @param twikiText
     * @param regex
     * @param regexReplacement
     * @return twikiText with all of the matches tokenized
     */
    public static String replaceAndTokenize(String twikiText,
                                            String regex,
                                            String regexReplacement) {
        return replaceAndTokenize(twikiText, regex, regexReplacement, 0);
    }

    /**
     * calls replaceAndTokenize with the multi-line flags of
     * Pattern.MULTILINE|Pattern.DOTALL
     *
     * @param twikiText
     * @param regex
     * @param regexReplacement
     * @return twikiText with all of the matches tokenized
     */
    public static String replaceAndTokenizeMultiLine(String twikiText,
                                                     String regex,
                                                     String regexReplacement) {
        return replaceAndTokenize(twikiText, regex, regexReplacement, Pattern.MULTILINE | Pattern.DOTALL);
    }

    /**
     * This method is very handy. Learn it, love it. It will save you time and
     * is great to use with things like links or other text/syntext that can be
     * easily 'messed' up by other converters.
     * <p/>
     * Basically it does these things:
     * 1) finds the match
     * 2) creates the replacement text
     * 3) puts the replacement into the TokenMap and hands back a token
     * 4) sticks the token into the original text
     * <p/>
     * Thus any successful match is then immune to further accidental tampering
     * by other converters
     *
     * @param twikiText
     * @param regex
     * @param regexReplacement
     * @return twikiText with all of the matches tokenized
     */
    public static String replaceAndTokenize(String twikiText,
                                            String regex,
                                            String regexReplacement,
                                            int flags) {

        if (flags == (Pattern.DOTALL | Pattern.MULTILINE)) {
            // enable multi line mode
            // not using the inline command (?s) doesn't seem to work
            regex = "(?s)" + regex;
        }
        // Compile the regex.
        Pattern pattern = Pattern.compile(regex, flags);
        // Get a Matcher based on the target string.
        Matcher matcher = pattern.matcher(twikiText);
        String retString = twikiText;
        // Find all the matches.
        while (matcher.find()) {
            // find the match
            String whatMatched = retString.substring(matcher.start(), matcher.end());
            // transform the match accodingly and into a token
            String replacedTheMatch = whatMatched.replaceFirst(regex, regexReplacement);
            String token = TokenMap.add(replacedTheMatch);
            //XXX Use these to debug problems
//            log.debug("regex = " + regex); //COMMENT
//            log.debug("regex replacement = " + regexReplacement); //COMMENT
//            log.debug("what matched = " + whatMatched); //COMMENT
//            log.debug("replacedTheMatch = " + replacedTheMatch); //COMMENT
//            log.debug("token = " + token); //COMMENT
            // stick the token into the original text
            retString = matcher.replaceFirst(token);
            // reset the matcher to deal with the new and altered retString
            matcher = pattern.matcher(retString);
        }
        return retString;
    }
}
