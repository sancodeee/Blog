package com.ws.util;

import lombok.extern.slf4j.Slf4j;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * MarkdownUtils
 *
 * @author wangsen
 * @date 2021/8/8
 */
@Slf4j
public class MarkdownUtils {

    /**
     * markdown格式转换成HTML格式
     *
     * @param markdown 减价
     * @return {@link String}
     */
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 增加扩展[标题锚点，表格生成]
     * Markdown转换成HTML
     *
     * @param markdown 减价
     * @return {@link String}
     */
    public static String markdownToHtmlExtensions(String markdown) {
        //h标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        //转换table的HTML
        List<Extension> tableExtension = Collections.singletonList(TablesExtension.create());
        Parser parser = Parser.builder().extensions(tableExtension).build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(context -> new CustomAttributeProvider()).build();
        return renderer.render(document);
    }

    /**
     * 处理标签的属性
     *
     * @author wangsen
     * @date 2024/01/14
     */
    static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            //改变a标签的target属性为_blank
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if (node instanceof TableBlock) {
                attributes.put("class", "ui celled table");
            }
        }
    }


    public static void main(String[] args) {
        String table = "| hello | hi   | 哈哈哈   |\n" + "| ----- | ---- | ----- |\n" + "| 斯维尔多  | 士大夫  | f啊    |\n" + "| 阿什顿发  | 非固定杆 | 撒阿什顿发 |\n" + "\n";
        log.info(markdownToHtmlExtensions(table));
    }
}
