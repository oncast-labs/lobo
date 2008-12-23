<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
   xmlns:lxslt="http://xml.apache.org/xslt">
   <xsl:output method="html" indent="yes" encoding="US-ASCII" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN" />
   <xsl:decimal-format decimal-separator="." grouping-separator="," />

   <xsl:template match="/">
      <html>
         <head>
            <title>Lobo Results</title>
            <style type="text/css">
               body { font:normal 68% verdana,arial,helvetica; color:#000000; } table tr td, table tr th { font-size:
               68%; } table.details tr th{ font-weight: bold; text-align:left; background:#a6caf0; } table.details tr
               td{ background:#eeeee0; } p { line-height:1.5em; margin-top:0.5em; margin-bottom:1.0em; } h1 { margin:
               0px 0px 5px; font: 165% verdana,arial,helvetica } h2 { margin-top: 1em; margin-bottom: 0.5em; font: bold
               125% verdana,arial,helvetica } h3 { margin-bottom: 0.5em; font: bold 115% verdana,arial,helvetica } h4 {
               margin-bottom: 0.5em; font: bold 100% verdana,arial,helvetica } h5 { margin-bottom: 0.5em; font: bold
               100% verdana,arial,helvetica } h6 { margin-bottom: 0.5em; font: bold 100% verdana,arial,helvetica }
               .Error { font-weight:bold; color:red; } .Failure { font-weight:bold; color:purple; } .Properties {
               text-align:right; }
            </style>
         </head>
         <body>
            <a name="top"></a>
            <xsl:call-template name="pageHeader" />

            <xsl:call-template name="caseList" />
            <hr size="1" width="95%" align="left" />

            <xsl:call-template name="cases" />
            <hr size="1" width="95%" align="left" />

         </body>
      </html>
   </xsl:template>

   <xsl:template name="caseList">
      <h2>Lobo Cases</h2>
      <table class="details" border="0" cellpadding="5" cellspacing="2" width="95%">
         <tr valign="top">
            <th width="80%">Name</th>
            <th>Scenarios</th>
            <th>Metrics</th>
         </tr>
         <xsl:for-each select="/profile-report-merge/profile-case">
            <xsl:sort select="@case-class" />
            <xsl:variable name="name" select="@case-class" />
            <xsl:variable name="scenarios" select="count(profile-scenario)" />
            <xsl:variable name="metrics" select="count(profile-scenario/profile-metric)" />
            <tr valign="top">
               <td>
                  <a href="#{@case-class}">
                     <xsl:value-of select="@case-class" />
                  </a>
               </td>
               <td>
                  <xsl:value-of select="$scenarios" />
               </td>
               <td>
                  <xsl:value-of select="$metrics" />
               </td>
            </tr>
         </xsl:for-each>
      </table>
   </xsl:template>

   <xsl:template name="cases">
      <xsl:for-each select="/profile-report-merge/profile-case">
         <xsl:sort select="@case-class" />
         <a name="{@case-class}"></a>
         <h3>
            Case
            <xsl:value-of select="@case-class" />
         </h3>
         <table class="details" border="0" cellpadding="5" cellspacing="2" width="95%">
            <tr valign="top">
               <th>Scenario</th>
               <th>Metric</th>
            </tr>
            <xsl:for-each select="profile-scenario">
               <tr valign="top">
                  <td>
                     <xsl:value-of select="@scenario-method" />
                  </td>
                  <td>
                     <xsl:for-each select="profile-metric">
                        <img src="{../../@case-class}/{../@scenario-method}-{@key}.jpeg" />
                     </xsl:for-each>
                  </td>
               </tr>
            </xsl:for-each>
         </table>
         <a href="#top">Back to top</a>
         <p />
         <p />
      </xsl:for-each>
   </xsl:template>

   <xsl:template name="pageHeader">
      <h1>Lobo Results</h1>
      <table width="100%">
         <tr>
            <td align="left"></td>
            <td align="right">
               Designed for use with
               <a href='http://www.oncast.com.br/dev/lobo'>Lobo</a>
               and
               <a href='http://ant.apache.org/ant'>Ant</a>
               .
            </td>
         </tr>
      </table>
      <hr size="1" />
   </xsl:template>

</xsl:stylesheet>