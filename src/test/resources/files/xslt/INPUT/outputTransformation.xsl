<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:con="http://container.service.esb.tecplata.com/">
	<xsl:output method="xml" indent="yes" />
	
	<xsl:strip-space  elements="*"/>
    
        <xsl:template match="/query-response">
        <xsl:choose>
            <xsl:when test="/query-response/data-table/rows/row">
                <xsl:apply-templates />
            </xsl:when>
            <xsl:otherwise>
                    <container />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
	
	<xsl:template match="/query-response/data-table/rows">
		<xsl:apply-templates />
	</xsl:template>
	<xsl:template match="row">
          <container>
            <id><xsl:value-of select="@primary-key" /></id>
            <unitNbr><xsl:value-of select="child::field[position()=1]/text()" /></unitNbr>
            <typeIso><xsl:value-of select="child::field[position()=2]/text()" /></typeIso>
            <category><xsl:value-of select="child::field[position()=3]/text()" /></category>
            <tState><xsl:value-of select="child::field[position()=4]/text()" /></tState>
            <position><xsl:value-of select="child::field[position()=5]/text()" /></position>
            <vState><xsl:value-of select="child::field[position()=6]/text()" /></vState>
            <lineOp><xsl:value-of select="child::field[position()=7]/text()" /></lineOp>
            <consignee><xsl:value-of select="child::field[position()=8]/text()" /></consignee>
            <consigneeName><xsl:value-of select="child::field[position()=9]/text()" /></consigneeName>

 			<xsl:if test="child::field[position()=3]/text() = 'Export'">
				<obActualVisit>
					<xsl:value-of select="child::field[position()=10]/text()" />
				</obActualVisit>
			</xsl:if>
			<xsl:if test="child::field[position()=3]/text() = 'Import'">
				<obActualVisit>
					<xsl:value-of select="child::field[position()=11]/text()" />
				</obActualVisit>
			</xsl:if>  
            
            <pod><xsl:value-of select="child::field[position()=12]/text()" /></pod>
            <length><xsl:value-of select="child::field[position()=13]/text()" /></length>
            <height><xsl:value-of select="child::field[position()=14]/text()" /></height>
            <cargoWeight><xsl:value-of select="child::field[position()=15]/text()" /></cargoWeight>
            <tareWeight><xsl:value-of select="child::field[position()=16]/text()" /></tareWeight>
            <weight><xsl:value-of select="child::field[position()=17]/text()" /></weight>
            <containerClass><xsl:value-of select="child::field[position()=18]/text()" /></containerClass>
            <bookingNumber><xsl:value-of select="child::field[position()=19]/text()" /></bookingNumber>
            <vesselVisitor />
         </container>
         
	</xsl:template>
	
	<xsl:template match="columns" />
	
</xsl:stylesheet>