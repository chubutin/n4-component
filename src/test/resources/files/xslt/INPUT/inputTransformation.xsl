<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:app="http://appointment.service.esb.tecplata.com/">
	<xsl:output method="xml" indent="yes" />
	
	<xsl:strip-space  elements="*"/>
	
		<xsl:template match="/query-response">
		<xsl:choose>
			<xsl:when test="/query-response/data-table/rows/row">
				<xsl:apply-templates />
			</xsl:when>
			<xsl:otherwise>
					<appointment/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="/query-response/data-table/rows">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="row">
		<appointment>
			<appointmentNbr><xsl:value-of select="child::field[position()=2]/text()" /></appointmentNbr>
			<container><xsl:value-of select="child::field[position()=15]/text()" /></container>
			<endDate><xsl:value-of select="child::field[position()=3]/text()" />T<xsl:value-of select="child::field[position()=5]/text()" /></endDate>
			<gateID><xsl:value-of select="child::field[position()=10]/text()" /></gateID>
			<licenseNbr><xsl:value-of select="child::field[position()=12]/text()" /></licenseNbr>
			<startDate><xsl:value-of select="child::field[position()=3]/text()" />T<xsl:value-of select="child::field[position()=4]/text()" /></startDate>
			<status><xsl:value-of select="child::field[position()=1]/text()" /></status>
			<transType><xsl:value-of select="child::field[position()=11]/text()" /></transType>
			<!-- <xRayRequired>false</xRayRequired> -->
		</appointment>
	</xsl:template>
	
	<xsl:template match="columns" />

</xsl:stylesheet>