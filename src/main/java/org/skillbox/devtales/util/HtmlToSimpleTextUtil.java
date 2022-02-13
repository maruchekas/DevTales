package org.skillbox.devtales.util;

import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

public class HtmlToSimpleTextUtil {

    public static String getSimpleTextFromHtml(String html, int resultLength) {
        Source htmlSource = new Source(html);
        Segment segment = new Segment(htmlSource, 0, resultLength);
        Renderer htmlRender = new Renderer(segment).setIncludeHyperlinkURLs(true);

        return htmlRender.toString();
    }
}
