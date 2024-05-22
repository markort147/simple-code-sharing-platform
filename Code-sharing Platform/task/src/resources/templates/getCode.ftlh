<!DOCTYPE html>
<#--<#assign snippets = snippets_as_json?eval>-->
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="stylesheet"
          href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>
<ul>
    <#list snippets as snippet>
        <li>
            <pre id="code_snippet"><code>${snippet.code}</code></pre>
            <span id="load_date">${snippet.date}</span>
            <br/>
            <#if snippet.is_time_restricted>
                <span id="time_restriction">${snippet.time}</span>
                <br/>
            </#if>
            <#if snippet.is_view_restricted>
                <span id="views_restriction">${snippet.views}</span>
                <br/>
            </#if>
            <br/>
        </li>
    </#list>
</ul>
</body>
</html>