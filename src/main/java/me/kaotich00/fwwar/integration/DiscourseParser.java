package me.kaotich00.fwwar.integration;

import me.kaotich00.fwwar.objects.discourse.DiscoursePost;

import java.util.LinkedHashMap;
import java.util.Map;

public class DiscourseParser {

    DiscoursePost discoursePost;

    public DiscourseParser(DiscoursePost discoursePost) {
        this.discoursePost = discoursePost;
    }

    public Map<String,String> getParsedData() {

        //String cooked = discoursePost.getContent();
        String cooked = "\u003cp\u003eNazione: Kaolandia\u003cbr\u003e\nCittà: [Kaotown]\u003cbr\u003e\nPartecipanti: [Kaotich00,Architetto,Madhok]\u003cbr\u003e\nTipologia: BOLT_WAR_FACTION\u003cbr\u003e\nPrezzo: 1000z\u003c/p\u003e\n\u003cp\u003eGDR: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc volutpat nibh a semper semper. Phasellus dolor arcu, cursus porta blandit quis, pellentesque quis odio. Pellentesque posuere dapibus efficitur. Maecenas iaculis erat metus, vitae eleifend eros luctus ac. Donec aliquet a velit ac aliquet. Vestibulum semper luctus augue, at finibus mauris eleifend ut. Integer ultrices metus nec feugiat vehicula.\u003c/p\u003e";

        cooked = cleanContent(cooked);

        Map<String,String> values = parseValues(cooked);

        return values;
    }

    private String cleanContent(String content) {
        String strRegEx = "<[^>]*>";

        String cleanContent = content.replaceAll(strRegEx, "");
        return cleanContent;
    }

    private Map<String,String> parseValues(String content) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for(String keyValue : content.split(" *\n *")) {
            String[] pairs = keyValue.split(" *: *", 2);
            map.put(pairs[0], pairs.length == 1 ? "" : pairs[1]);
        }
        return map;
    }

}
