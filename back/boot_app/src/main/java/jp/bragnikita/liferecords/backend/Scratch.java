package jp.bragnikita.liferecords.backend;

import org.apache.commons.lang3.ArrayUtils;
import org.broadbear.link.preview.SourceContent;
import org.broadbear.link.preview.TextCrawler;

class Scratch {
    public static void main(String[] args) {
        final SourceContent content = TextCrawler.scrape("http://example.com", 0);
        System.out.println(content.getTitle());
        System.out.println(content.getDescription());
        System.out.println(content.getImages());
        System.out.println(content.getFinalUrl());
        System.out.println(ArrayUtils.toString(content.getUrlData()));
    }
}