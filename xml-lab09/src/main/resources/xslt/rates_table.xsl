<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Kursy walut – tabela</title>
                <style>
                    body  { font-family: Arial, sans-serif; margin: 20px; }
                    h1    { color: #333; }
                    table { border-collapse: collapse; width: 70%; }
                    th    { background: #4a90d9; color: white; padding: 8px 12px; text-align: left; }
                    td    { border: 1px solid #ccc; padding: 6px 12px; }
                    tr:nth-child(even) td { background: #f2f7ff; }
                </style>
            </head>
            <body>
                <h1>Kursy walut USD</h1>
                <table>
                    <tr>
                        <th>#</th>
                        <th>Waluta</th>
                        <th>Opis / Kurs</th>
                    </tr>
                    <xsl:for-each select="//item">
                        <tr>
                            <td><xsl:number/></td>
                            <td><xsl:value-of select="title"/></td>
                            <td><xsl:value-of select="description"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>