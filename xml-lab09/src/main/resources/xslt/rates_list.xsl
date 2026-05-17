<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <html>
        <head>
            <meta charset="UTF-8"/>
            <title>Kursy walut – lista</title>
            <style>
                body { font-family: Georgia, serif; margin: 20px; background: #fafafa; }
                h1   { color: #555; border-bottom: 2px solid #aaa; padding-bottom: 6px; }
                ul   { list-style: none; padding: 0; }
                li   { padding: 5px 10px; border-left: 4px solid #4a90d9; margin: 4px 0;
                       background: white; box-shadow: 1px 1px 3px #ddd; }
                .cur { font-weight: bold; color: #333; }
                .desc{ color: #666; font-size: 0.9em; }
            </style>
        </head>
        <body>
            <h1><xsl:value-of select="rss/channel/title"/></h1>
            <ul>
                <xsl:for-each select="rss/channel/item">
                    <li>
                        <span class="cur"><xsl:value-of select="title"/> — </span>
                        <span class="desc"><xsl:value-of select="description"/></span>
                    </li>
                </xsl:for-each>
            </ul>
        </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
