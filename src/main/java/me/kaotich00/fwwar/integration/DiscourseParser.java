package me.kaotich00.fwwar.integration;

import it.forgottenworld.fwechelonapi.discourse.DiscoursePost;

import java.util.LinkedHashMap;
import java.util.Map;

public class DiscourseParser {

    DiscoursePost discoursePost;

    public DiscourseParser(DiscoursePost discoursePost) {
        this.discoursePost = discoursePost;
    }

    public Map<String,String> getParsedData() {

        String cooked = discoursePost.getContent();

        cooked = cleanContent(cooked);

        Map<String,String> values = parseValues(cooked);

        return values;
    }

    private String cleanContent(String content) {
        String strRegEx = "<[^>]*>";

        return content.replaceAll(strRegEx, "");
    }

    private Map<String,String> parseValues(String content) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for(String keyValue : content.split(" *\n *")) {
            String[] pairs = keyValue.split(" *: *", 2);
            map.put(pairs[0].toLowerCase(), pairs.length == 1 ? "" : pairs[1]);
        }
        map.remove("gdr");
        return map;
    }

}
