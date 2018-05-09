package com.grt.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.avaya.grt.mappers.ExpandedSolutionElement;
import com.avaya.grt.mappers.SiteList;
import com.avaya.grt.mappers.TechnicalRegistration;
import com.grt.util.GenericSort;

public class DynamicMailContentRenderer {
	private static final Logger logger = Logger.getLogger(DynamicMailContentRenderer.class);
	
	//private static final String sslvpn = "<tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Material Code</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Serial Number</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Service Name</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Alarm Id</b></span></p></td></tr><tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&MC</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SN</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&ServiceName</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&AlarmId</span></p></td></tr>";
	private static final String sslvpn = "<tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Material Code</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Serial Number</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Service Name</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Alarm Id</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SEID</b></span></p></td></tr><tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&MC</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SN</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&ServiceName</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&AlarmId</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&Seid</span></p></td></tr>";	
	private static final String srClosure = "<tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Material Code</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Se Code</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Service Request Number</b></span></p></td></tr><tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&MC</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SECODE</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SRNumber</span></p></td></tr>";
	private static final String tobUpdate = "<tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Material Code</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Se Code</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SEID</b></span></p></td></tr><tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&MC</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SECODE</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SEID</span></p></td></tr>&installScript";
	private static final String tobUpdateInstallScript = "<tr><td width='568' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;' colspan='3'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Install Script</b><br>&InstallScript</span></p></td></tr>";
	private static final String modem = "<tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Material Code</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Dial In#</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SID</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>MID</b></span></p></td></tr><tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&MC</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&DialIn</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&sid</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&mid</span></p></td></tr>&explodedAssets&installScript";
	private static final String ip = "<tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Material Code</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>IP Address</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SID</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>MID</b></span></p></td></tr><tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&MC</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&IPAddress</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&sid</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&mid</span></p></td></tr>&explodedAssets&installScript";
	private static final String ipModemInstallScript = "<tr><td width='568' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;' colspan='4'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Install Script</b><br>&InstallScript</span></p></td></tr>";
	private static final String salRecordBuilding = "<tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Material Code</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SAL Gateway</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SID</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>MID</b></span></p></td></tr><tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&MC</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&gatewaySeid</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&sid</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&mid</span></p></td></tr>&explodedAssets&installScript";
	private static final String explodedAssetHeader = "<tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SE Code</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SEID</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>IP Address</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Alarm Id</b></span></p></td></tr>";
	private static final String explodedAssetRow = "<tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&Secode</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SEID</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&IPAddress</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&AlarmId</span></p></td></tr>";
	private static final String salInstallScript = "<tr><td width='568' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;' colspan='4'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Install Script</b><br>&InstallScript</span></p></td></tr>";
	private static final String salMigration = "<tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Material Code</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SAL Gateway</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SE Code</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SEID</b></span></p></td></tr><tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&MC</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&gateway</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SeCode</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SEID</span></p></td></tr>&installScript";
	//private static final String salStepBHeader = "<tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Material Code</b></span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SE Code</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SEID</b></span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Model</b></span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>AlarmId</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Last Alarm Received Date</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SAL Gateway</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Device Status</b></span></p></td></tr>";
	private static final String salStepBHeader = "<tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Material Code</b></span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SE Code</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SEID</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SID</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>MID</b></span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Model</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Remote Access Eligible</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Remote Access Selected</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Transport Alarm Eligible</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Transport Alarm Selected</b></span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>AlarmId</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Last Alarm Received Date</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>SAL Gateway</b></span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'><b>Device Status</b></span></p></td></tr>";
	//private static final String salStepBRow = "<tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&MC</span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SeCode</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SEID</span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&Model</span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&AlarmId</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&AlarmReceivedDate</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SALGW</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&DeviceStatus</span></p></td></tr>";
	private static final String salStepBRow = "<tr><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&MC</span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SeCode</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SEID</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SID</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&MID</span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&Model</span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&RemoteAccessEligible</span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&RemoteAccessSelected</span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&TransportAlarmEligible</span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&TransportAlarmSelected</span></p></td><td width='100' style='border-style: solid solid solid solid; border-width: 1pt 1pt 1pt; padding: 6pt 0cm 6pt 15pt; width: 138.75pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&AlarmId</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&AlarmReceivedDate</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&SALGW</span></p></td><td width='100' style='border-style: solid solid solid none; border-width: 1pt 1pt 1pt medium; padding: 6pt 0cm 6pt 15pt; width: 255pt;'><p class='MsoNormal'><span lang='EN-US' style='font-size: 8.5pt;'>&DeviceStatus</span></p></td></tr>";
		
	//&Secode, &SEID, &IPAddress, &AlarmId
	public static String getDynamicDataForExplodedAsset(String seCode, String seid, String ipAddress, String alarmId) {
		logger.debug("Entering getDynamicDataForExplodedAsset: alarmId" + alarmId + " ipAddress:" + ipAddress + " seCode:" + seCode + " seid:" + seid);
		String dynamicData = " ";
		try {
			if(StringUtils.isNotEmpty(seCode)) {
				dynamicData = explodedAssetRow.replaceFirst("&Secode", seCode);
			} else {
				dynamicData = explodedAssetRow.replaceFirst("&Secode", " ");
			}
			
			if(StringUtils.isNotEmpty(seid)) {
				dynamicData = dynamicData.replaceFirst("&SEID", seid);
			} else {
				dynamicData = dynamicData.replaceFirst("&SEID", " ");
			}
			
			if(StringUtils.isNotEmpty(alarmId)) {
				dynamicData = dynamicData.replaceFirst("&AlarmId", alarmId);
			} else {
				dynamicData = dynamicData.replaceFirst("&AlarmId", " ");
			}
			
			if(StringUtils.isNotEmpty(ipAddress)) {
				dynamicData = dynamicData.replaceFirst("&IPAddress", ipAddress);
			} else {
				dynamicData = dynamicData.replaceFirst("&IPAddress", " ");
			}
			
		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting getDynamicDataForExplodedAsset: alarmId" + alarmId + " ipAddress:" + ipAddress + " seCode:" + seCode + " seid:" + seid);
		}
		return dynamicData;
	}
	
	//&MC, &SeCode, &SEID, &Model, &AlarmId, &AlarmReceivedDate, &SALGW, &DeviceStatus, &SID, &MID
	public static String getDynamicDataForStepBRow(String mc, String seCode, String model, String alarmId, String seid, String lastAlarmReceivedDate, String salgw, String status, String sid, String mid, String remoteAccess, boolean isSelectForRemoteAccess, String transportAlarm, boolean isSelectForAlarming) {
		logger.debug("Entering getDynamicDataForStepBRow: mc:" + mc + " alarmId:" + alarmId + " model:" + model + " seCode:" + seCode + " seid:" + seid + " lastAlarmReceivedDate:" + lastAlarmReceivedDate + " status:" + status);
		String dynamicData = " ";
		try {
			
			if(StringUtils.isNotEmpty(mc)) {
				dynamicData = salStepBRow.replaceFirst("&MC", mc);
			} else {
				dynamicData = salStepBRow.replaceFirst("&MC", " ");
			}
			
			if(StringUtils.isNotEmpty(seCode)) {
				dynamicData = dynamicData.replaceFirst("&SeCode", seCode);
			} else {
				dynamicData = dynamicData.replaceFirst("&SeCode", " ");
			}
			
			if(StringUtils.isNotEmpty(model)) {
				dynamicData = dynamicData.replaceFirst("&Model", model);
			} else {
				dynamicData = dynamicData.replaceFirst("&Model", " ");
			}
			
			if(StringUtils.isNotEmpty(seid)) {
				dynamicData = dynamicData.replaceFirst("&SEID", seid);
			} else {
				dynamicData = dynamicData.replaceFirst("&SEID", " ");
			}
			
			if(StringUtils.isNotEmpty(sid)) {
				dynamicData = dynamicData.replaceFirst("&SID", sid);
			} else {
				dynamicData = dynamicData.replaceFirst("&SID", " ");
			}
			
			if(StringUtils.isNotEmpty(mid)) {
				dynamicData = dynamicData.replaceFirst("&MID", mid);
			} else {
				dynamicData = dynamicData.replaceFirst("&MID", " ");
			}
			
			if(StringUtils.isNotEmpty(alarmId)) {
				dynamicData = dynamicData.replaceFirst("&AlarmId", alarmId);
			} else {
				dynamicData = dynamicData.replaceFirst("&AlarmId", " ");
			}
			
			if(StringUtils.isNotEmpty(lastAlarmReceivedDate)) {
				dynamicData = dynamicData.replaceFirst("&AlarmReceivedDate", lastAlarmReceivedDate);
			} else {
				dynamicData = dynamicData.replaceFirst("&AlarmReceivedDate", " ");
			}
			
			if(StringUtils.isNotEmpty(salgw)) {
				dynamicData = dynamicData.replaceFirst("&SALGW", salgw);
			} else {
				dynamicData = dynamicData.replaceFirst("&SALGW", " ");
			}
			
			if(StringUtils.isNotEmpty(status)) {
				dynamicData = dynamicData.replaceFirst("&DeviceStatus", status);
			} else {
				dynamicData = dynamicData.replaceFirst("&DeviceStatus", " ");
			}
			
			if(StringUtils.isNotEmpty(remoteAccess)) {
				dynamicData = dynamicData.replaceFirst("&RemoteAccessEligible", remoteAccess);
			} else {
				dynamicData = dynamicData.replaceFirst("&RemoteAccessEligible", " ");
			}
			
			if(isSelectForRemoteAccess) {
				dynamicData = dynamicData.replaceFirst("&RemoteAccessSelected", "Y");
			} else {
				dynamicData = dynamicData.replaceFirst("&RemoteAccessSelected", "N");
			}
			
			if(StringUtils.isNotEmpty(transportAlarm)) {
				dynamicData = dynamicData.replaceFirst("&TransportAlarmEligible", transportAlarm);
			} else {
				dynamicData = dynamicData.replaceFirst("&TransportAlarmEligible", " ");
			}
			
			if(isSelectForAlarming) {
				dynamicData = dynamicData.replaceFirst("&TransportAlarmSelected", "Y");
			} else {
				dynamicData = dynamicData.replaceFirst("&TransportAlarmSelected", "N");
			}
			
		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Entering getDynamicDataForStepBRow: mc:" + mc + " alarmId:" + alarmId + " model:" + model + " seCode:" + seCode + " seid:" + seid + " lastAlarmReceivedDate:" + lastAlarmReceivedDate + " status:" + status);
		}
		return dynamicData;
	}
	
	//&MC, &gatewaySeid, &sid, &mid, &explodedAssets, &installScript
	public static String getDynamicDataForSALRecordBuilding(String materialCode, String gatewaySeid, String sid, String mid, String seCode, String seid, Set<ExpandedSolutionElement> explodedAssets, String installScript) {
		logger.debug("Entering getDynamicDataForSALRecordBuilding: materialCode" + materialCode + " gatewaySeid:" + gatewaySeid + " seCode:" + seCode + " seid:" + seid + " sid:" + sid + " mid:" + sid + " installScript" + installScript);
		if(explodedAssets != null) {
			logger.debug("explodedAssets.size:" + explodedAssets.size());
		}
		String dynamicData = " ";
		try {
			if(StringUtils.isNotEmpty(materialCode)) {
				dynamicData = salRecordBuilding.replaceFirst("&MC", materialCode);
			} else {
				dynamicData = salRecordBuilding.replaceFirst("&MC", " ");
			}
			
			if(StringUtils.isNotEmpty(gatewaySeid)) {
				dynamicData = dynamicData.replaceFirst("&gatewaySeid", gatewaySeid);
			} else {
				dynamicData = dynamicData.replaceFirst("&gatewaySeid", " ");
			}
			
			if(StringUtils.isNotEmpty(sid)) {
				dynamicData = dynamicData.replaceFirst("&sid", sid);
			} else {
				dynamicData = dynamicData.replaceFirst("&sid", " ");
			}
			
			if(StringUtils.isNotEmpty(mid)) {
				dynamicData = dynamicData.replaceFirst("&mid", mid);
			} else {
				dynamicData = dynamicData.replaceFirst("&mid", " ");
			}
			
			String explodedAssetData = explodedAssetHeader;
			if(explodedAssets != null && explodedAssets.size() > 0) {
				List<ExpandedSolutionElement> children = new ArrayList(explodedAssets);
				GenericSort gs = new GenericSort("seID", true);
				Collections.sort(children, gs);
				
				List<String> dupliCateEment=new ArrayList<String>();
				
				for (ExpandedSolutionElement expandedSolutionElement : children) {
					if(!dupliCateEment.contains(expandedSolutionElement.getSeID()))
						explodedAssetData += getDynamicDataForExplodedAsset(expandedSolutionElement.getSeCode(), expandedSolutionElement.getSeID(), expandedSolutionElement.getIpAddress(), expandedSolutionElement.getAlarmId());
					dupliCateEment.add(expandedSolutionElement.getSeID());
				}
			} else {
				explodedAssetData += getDynamicDataForExplodedAsset(seCode, seid, null, null);
			}
		
			if(StringUtils.isNotEmpty(explodedAssetData)) {
				dynamicData = dynamicData.replaceFirst("&explodedAssets", explodedAssetData);
			} else {
				dynamicData = dynamicData.replaceFirst("&explodedAssets", " ");
			}
			
			if(StringUtils.isNotEmpty(installScript)) {
				dynamicData = dynamicData.replaceFirst("&installScript", salInstallScript.replaceFirst("&InstallScript", "<pre>" + installScript  + "</pre>"));
			} else {
				dynamicData = dynamicData.replaceFirst("&installScript", " ");
			}
		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting getDynamicDataForSALRecordBuilding: materialCode" + materialCode + " gatewaySeid:" + gatewaySeid + " seCode:" + seCode + " seid:" + seid + " sid:" + sid + " mid:" + sid + " installScript" + installScript);
		}
		return dynamicData;
	}
	
	//&MC, &DialIn, &sid, &mid, &explodedAssets, &installScript
	public static String getDynamicDataForModem(String materialCode, String dialIn, String sid, String mid, String seCode, String seid, Set<ExpandedSolutionElement> explodedAssets, String installScript) {
		logger.debug("Entering getDynamicDataForModem: materialCode" + materialCode + " dialIn:" + dialIn + " seCode:" + seCode + " seid:" + seid + " sid:" + sid + " mid:" + sid + " installScript" + installScript);
		if(explodedAssets != null) {
			logger.debug("explodedAssets.size:" + explodedAssets.size());
		}
		String dynamicData = " ";
		try {
			if(StringUtils.isNotEmpty(materialCode)) {
				dynamicData = modem.replaceFirst("&MC", materialCode);
			} else {
				dynamicData = modem.replaceFirst("&MC", " ");
			}
			
			if(StringUtils.isNotEmpty(dialIn)) {
				dynamicData = dynamicData.replaceFirst("&DialIn", dialIn);
			} else {
				dynamicData = dynamicData.replaceFirst("&DialIn", " ");
			}
			
			if(StringUtils.isNotEmpty(sid)) {
				dynamicData = dynamicData.replaceFirst("&sid", sid);
			} else {
				dynamicData = dynamicData.replaceFirst("&sid", " ");
			}
			
			if(StringUtils.isNotEmpty(mid)) {
				dynamicData = dynamicData.replaceFirst("&mid", mid);
			} else {
				dynamicData = dynamicData.replaceFirst("&mid", " ");
			}
			
			String explodedAssetData = explodedAssetHeader;
			if(explodedAssets != null && explodedAssets.size() > 0) {
				List<ExpandedSolutionElement> children = new ArrayList(explodedAssets);
				GenericSort gs = new GenericSort("seID", true);
				Collections.sort(children, gs);
				
				List<String> dupliCateEment=new ArrayList<String>();
				
				for (ExpandedSolutionElement expandedSolutionElement : children) {
					if(!dupliCateEment.contains(expandedSolutionElement.getSeID()))
						explodedAssetData += getDynamicDataForExplodedAsset(expandedSolutionElement.getSeCode(), expandedSolutionElement.getSeID(), expandedSolutionElement.getIpAddress(), expandedSolutionElement.getAlarmId());
					dupliCateEment.add(expandedSolutionElement.getSeID());
				}
				
			} else {
				explodedAssetData += getDynamicDataForExplodedAsset(seCode, seid, null, null);
			}
		
			if(StringUtils.isNotEmpty(explodedAssetData)) {
				dynamicData = dynamicData.replaceFirst("&explodedAssets", explodedAssetData);
			} else {
				dynamicData = dynamicData.replaceFirst("&explodedAssets", " ");
			}
			
			if(StringUtils.isNotEmpty(installScript)) {
				dynamicData = dynamicData.replaceFirst("&installScript", ipModemInstallScript.replaceFirst("&InstallScript", "<pre>" + installScript  + "</pre>"));
			} else {
				dynamicData = dynamicData.replaceFirst("&installScript", " ");
			}
		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting getDynamicDataForModem: materialCode" + materialCode + " dialIn:" + dialIn + " seCode:" + seCode + " seid:" + seid + " sid:" + sid + " mid:" + sid + " installScript" + installScript);
		}
		return dynamicData;
	}
	
	//&MC, &IPAddress, &sid, &mid, &explodedAssets, &installScript
	public static String getDynamicDataForIP(String materialCode, String ipAddress, String sid, String mid, String seCode, String seid, Set<ExpandedSolutionElement> explodedAssets, String installScript) {
		logger.debug("Entering getDynamicDataForIP: materialCode" + materialCode + " ipAddress:" + ipAddress + " seCode:" + seCode + " seid:" + seid + " sid:" + sid + " mid:" + sid + " installScript" + installScript);
		if(explodedAssets != null) {
			logger.debug("explodedAssets.size:" + explodedAssets.size());
		}
		String dynamicData = " ";
		try {
			if(StringUtils.isNotEmpty(materialCode)) {
				dynamicData = ip.replaceFirst("&MC", materialCode);
			} else {
				dynamicData = ip.replaceFirst("&MC", " ");
			}
			
			if(StringUtils.isNotEmpty(ipAddress)) {
				dynamicData = dynamicData.replaceFirst("&IPAddress", ipAddress);
			} else {
				dynamicData = dynamicData.replaceFirst("&IPAddress", " ");
			}
			
			if(StringUtils.isNotEmpty(sid)) {
				dynamicData = dynamicData.replaceFirst("&sid", sid);
			} else {
				dynamicData = dynamicData.replaceFirst("&sid", " ");
			}
			
			if(StringUtils.isNotEmpty(mid)) {
				dynamicData = dynamicData.replaceFirst("&mid", mid);
			} else {
				dynamicData = dynamicData.replaceFirst("&mid", " ");
			}
			
			String explodedAssetData = explodedAssetHeader;
			if(explodedAssets != null && explodedAssets.size() > 0) {
				List<ExpandedSolutionElement> children = new ArrayList(explodedAssets);
				GenericSort gs = new GenericSort("seID", true);
				Collections.sort(children, gs);
				
				List<String> dupliCateEment=new ArrayList<String>();
				
				for (ExpandedSolutionElement expandedSolutionElement : children) {
					if(!dupliCateEment.contains(expandedSolutionElement.getSeID()))
						explodedAssetData += getDynamicDataForExplodedAsset(expandedSolutionElement.getSeCode(), expandedSolutionElement.getSeID(), expandedSolutionElement.getIpAddress(), expandedSolutionElement.getAlarmId());
					dupliCateEment.add(expandedSolutionElement.getSeID());
				}
			} else {
				explodedAssetData += getDynamicDataForExplodedAsset(seCode, seid, null, null);
			}
		
			if(StringUtils.isNotEmpty(explodedAssetData)) {
				dynamicData = dynamicData.replaceFirst("&explodedAssets", explodedAssetData);
			} else {
				dynamicData = dynamicData.replaceFirst("&explodedAssets", " ");
			}
			
			if(StringUtils.isNotEmpty(installScript)) {
				dynamicData = dynamicData.replaceFirst("&installScript", ipModemInstallScript.replaceFirst("&InstallScript", "<pre>" + installScript  + "</pre>"));
			} else {
				dynamicData = dynamicData.replaceFirst("&installScript", " ");
			}
		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting getDynamicDataForIP: materialCode" + materialCode + " ipAddress:" + ipAddress + " seCode:" + seCode + " seid:" + seid + " sid:" + sid + " mid:" + sid + " installScript" + installScript);
		}
		return dynamicData;
	}
	
	// &MC, &gateway, &SeCode, &SEID, &installScript
	public static String getDynamicDataForSALMigration(String materialCode, String gateway, String seCode, String seid, String installScript) {
		logger.debug("Entering getDynamicDataForSALMigration: materialCode" + materialCode + " gateway:" + gateway + " seCode:" + seCode + " seid:" + seid + " installScript" + installScript);
		String dynamicData = " ";
		try {
			if(StringUtils.isNotEmpty(materialCode)) {
				dynamicData = salMigration.replaceFirst("&MC", materialCode);
			} else {
				dynamicData = salMigration.replaceFirst("&MC", " ");
			}
			
			if(StringUtils.isNotEmpty(gateway)) {
				dynamicData = dynamicData.replaceFirst("&gateway", gateway);
			} else {
				dynamicData = dynamicData.replaceFirst("&gateway", " ");
			}
			
			if(StringUtils.isNotEmpty(seCode)) {
				dynamicData = dynamicData.replaceFirst("&SeCode", seCode);
			} else {
				dynamicData = dynamicData.replaceFirst("&SeCode", " ");
			}
			
			if(StringUtils.isNotEmpty(seid)) {
				dynamicData = dynamicData.replaceFirst("&SEID", seid);
			} else {
				dynamicData = dynamicData.replaceFirst("&SEID", " ");
			}
			
			if(StringUtils.isNotEmpty(installScript)) {
				dynamicData = dynamicData.replaceFirst("&installScript", salInstallScript.replaceFirst("&InstallScript", "<pre>" + installScript  + "</pre>"));
			} else {
				dynamicData = dynamicData.replaceFirst("&installScript", " ");
			}
		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting getDynamicDataForSALMigration: materialCode" + materialCode + " gateway:" + gateway + " seCode:" + seCode + " seid:" + seid + " installScript" + installScript);
		}
		return dynamicData;
	}
	
	//	&MC, &SECODE, &SEID, &installScript
	public static String getDynamicDataForTobUpdate(String materialCode, String seCode, String seid, String installScript) {
		logger.debug("Entering getDynamicDataForTobUpdate: materialCode" + materialCode + " seCode:" + seCode + " seid:" + seid + " installScript" + installScript);
		String dynamicData = " ";
		try {
			if(StringUtils.isNotEmpty(materialCode)) {
				dynamicData = tobUpdate.replaceFirst("&MC", materialCode);
			} else {
				dynamicData = tobUpdate.replaceFirst("&MC", " ");
			}
			
			if(StringUtils.isNotEmpty(seCode)) {
				dynamicData = dynamicData.replaceFirst("&SECODE", seCode);
			} else {
				dynamicData = dynamicData.replaceFirst("&SECODE", " ");
			}
			
			if(StringUtils.isNotEmpty(seid)) {
				dynamicData = dynamicData.replaceFirst("&SEID", seid);
			} else {
				dynamicData = dynamicData.replaceFirst("&SEID", " ");
			}
			
			if(StringUtils.isNotEmpty(installScript)) {
				dynamicData = dynamicData.replaceFirst("&installScript", tobUpdateInstallScript.replaceFirst("&InstallScript", "<pre>" + installScript  + "</pre>"));
			} else {
				dynamicData = dynamicData.replaceFirst("&installScript", " ");
			}
		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting getDynamicDataForTobUpdate: materialCode" + materialCode + " seCode:" + seCode + " seid:" + seid + " installScript" + installScript);
		}
		return dynamicData;
	}
	
	//	&MC, &SECODE, &SRNumber
	public static String getDynamicDataForSRClosure(String materialCode, String seCode, String srNumber) {
		logger.debug("Entering getDynamicDataForSRClosure: materialCode" + materialCode + " seCode:" + seCode + " srNumber:" + srNumber);
		String dynamicData = " ";
		try {
			if(StringUtils.isNotEmpty(materialCode)) {
				dynamicData = srClosure.replaceFirst("&MC", materialCode);
			} else {
				dynamicData = srClosure.replaceFirst("&MC", " ");
			}
			
			if(StringUtils.isNotEmpty(seCode)) {
				dynamicData = dynamicData.replaceFirst("&SECODE", seCode);
			} else {
				dynamicData = dynamicData.replaceFirst("&SECODE", " ");
			}
			
			if(StringUtils.isNotEmpty(srNumber)) {
				dynamicData = dynamicData.replaceFirst("&SRNumber", srNumber);
			} else {
				dynamicData = dynamicData.replaceFirst("&SRNumber", " ");
			}
		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting getDynamicDataForSRClosure: materialCode" + materialCode + " seCode:" + seCode + " srNumber:" + srNumber);
		}
		return dynamicData;
	}
	

	public static String getDynamicDataForStepBCompletion(List<TechnicalRegistration> trs, List<SiteList> sls) {
		logger.debug("Entering getDynamicDataForStepBCompletion");
		String dynamicData = " ";
		try {
			dynamicData = salStepBHeader;
			if(trs != null) {
				for (TechnicalRegistration tr : trs) {
					dynamicData += getDynamicDataForStepBRow(tr.getTechnicalOrder().getMaterialCode(), tr.getSolutionElement(), tr.getModel(), tr.getAlarmId(), tr.getSolutionElementId(), tr.getDeviceLastAlarmReceivedDate(), tr.getFullGatewaySeid(), tr.getDeviceStatus(), tr.getSid(), tr.getMid(), tr.getRemoteAccess(), tr.isSelectForRemoteAccess(), tr.getTransportAlarm(), tr.isSelectForAlarming());
					if(tr.getExpSolutionElements() != null && tr.getExpSolutionElements().size()>0) {
						List<ExpandedSolutionElement> children = tr.getExpSolutionElements();
						GenericSort gs = new GenericSort("seID", true);
						Collections.sort(children, gs);
						for (ExpandedSolutionElement expandedSolutionElement : children) {
							if(expandedSolutionElement != null && StringUtils.isNotEmpty(expandedSolutionElement.getSeID()) && StringUtils.isNotEmpty(tr.getSolutionElementId()) && !expandedSolutionElement.getSeID().equals(tr.getSolutionElementId())) {
								dynamicData += getDynamicDataForStepBRow(" ", expandedSolutionElement.getSeCode(), tr.getModel(), expandedSolutionElement.getAlarmId(), expandedSolutionElement.getSeID(), expandedSolutionElement.getDeviceLastAlarmReceivedDate(), tr.getFullGatewaySeid(), expandedSolutionElement.getDeviceStatus(), tr.getSid(), tr.getMid(), expandedSolutionElement.getRemoteAccessEligible(), expandedSolutionElement.isSelectForRemoteAccess(), expandedSolutionElement.getTransportAlarmEligible(), expandedSolutionElement.isSelectForAlarming());
							}
						}
					}
				}
			} else if(sls != null) {
				for (SiteList sl : sls) {
					dynamicData += getDynamicDataForStepBRow(sl.getMaterialCode(), sl.getSeCode(), sl.getModel(), sl.getAlarmId(), sl.getSolutionElementId(), sl.getDeviceLastAlarmReceivedDate(), sl.getFullGatewaySeid(), sl.getDeviceStatus(), sl.getSid(), sl.getMid(), sl.getRemoteAccess(), sl.isSelectForRemoteAccess(), sl.getTransportAlarm(), sl.isSelectForAlarming());
					if(sl.getExplodedSolutionElements() != null && sl.getExplodedSolutionElements().size()>0) {
						List<ExpandedSolutionElement> children = sl.getExpSolutionElements();
						GenericSort gs = new GenericSort("seID", true);
						Collections.sort(children, gs);
						for (ExpandedSolutionElement expandedSolutionElement : children) {
							if(expandedSolutionElement != null && StringUtils.isNotEmpty(expandedSolutionElement.getSeID()) && StringUtils.isNotEmpty(sl.getSolutionElementId()) && !expandedSolutionElement.getSeID().equals(sl.getSolutionElementId())) {
								dynamicData += getDynamicDataForStepBRow(" ", expandedSolutionElement.getSeCode(), sl.getModel(), expandedSolutionElement.getAlarmId(), expandedSolutionElement.getSeID(), expandedSolutionElement.getDeviceLastAlarmReceivedDate(), sl.getFullGatewaySeid(), expandedSolutionElement.getDeviceStatus(), sl.getSid(), sl.getMid(), expandedSolutionElement.getRemoteAccessEligible(), expandedSolutionElement.isSelectForRemoteAccess(), expandedSolutionElement.getTransportAlarmEligible(), expandedSolutionElement.isSelectForAlarming());
							}
						}
					}
				}
			}  
		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting getDynamicDataForStepBCompletion");
		}
		return dynamicData;
	}
	
	//&MC, &SN, &ServiceName, &AlarmId, &Seid
	public static String getDynamicDataForSSLVPN(String materialCode, String serialNumber, String serviceName, String alarmId, String seid) {
		logger.debug("Entering getDynamicDataForSSLVPN: materialCode" + materialCode + " serialNumber:" + serialNumber + " serviceName:" + serviceName + " alarmId:" + alarmId +  " SEID: " + seid);
		String dynamicData = " ";
		try {
			if(StringUtils.isNotEmpty(materialCode)) {
				dynamicData = sslvpn.replaceFirst("&MC", materialCode);
			} else {
				dynamicData = sslvpn.replaceFirst("&MC", " ");
			}
			
			if(StringUtils.isNotEmpty(serialNumber)) {
				dynamicData = dynamicData.replaceFirst("&SN", serialNumber);
			} else {
				dynamicData = dynamicData.replaceFirst("&SN", " ");
			}
			
			if(StringUtils.isNotEmpty(serviceName)) {
				dynamicData = dynamicData.replaceFirst("&ServiceName", serviceName);
			} else {
				dynamicData = dynamicData.replaceFirst("&ServiceName", " ");
			}
			
			if(StringUtils.isNotEmpty(alarmId)) {
				dynamicData = dynamicData.replaceFirst("&AlarmId", alarmId);
			} else {
				dynamicData = dynamicData.replaceFirst("&AlarmId", " ");
			}
			
			//GRT 4.0 : UAT Defect 548
			if(StringUtils.isNotEmpty(seid)) {
				dynamicData = dynamicData.replaceFirst("&Seid", seid);
			} else {
				dynamicData = dynamicData.replaceFirst("&Seid", " ");
			}
		} catch (Throwable throwable) {
			logger.error("", throwable);
		} finally {
			logger.debug("Exiting getDynamicDataForSSLVPN: materialCode" + materialCode + " serialNumber:" + serialNumber + " serviceName:" + serviceName + " alarmId:" + alarmId);
		}
		return dynamicData;
	}
	
	
	//&MC, &DialIn, &sid, &mid, &explodedAssets, &installScript
		public static String getDynamicDataForNoConnectivity(String materialCode, String dialIn, String sid, String mid, String seCode, String seid, Set<ExpandedSolutionElement> explodedAssets, String installScript) {
			logger.debug("Entering getDynamicDataForModem: materialCode" + materialCode + " dialIn:" + dialIn + " seCode:" + seCode + " seid:" + seid + " sid:" + sid + " mid:" + sid + " installScript" + installScript);
			if(explodedAssets != null) {
				logger.debug("explodedAssets.size:" + explodedAssets.size());
			}
			String dynamicData = " ";
			try {
				if(StringUtils.isNotEmpty(materialCode)) {
					dynamicData = modem.replaceFirst("&MC", materialCode);
				} else {
					dynamicData = modem.replaceFirst("&MC", " ");
				}
			
				if(StringUtils.isNotEmpty(sid)) {
					dynamicData = dynamicData.replaceFirst("&sid", sid);
				} else {
					dynamicData = dynamicData.replaceFirst("&sid", " ");
				}
				
				if(StringUtils.isNotEmpty(mid)) {
					dynamicData = dynamicData.replaceFirst("&mid", mid);
				} else {
					dynamicData = dynamicData.replaceFirst("&mid", " ");
				}
				
				String explodedAssetData = explodedAssetHeader;
				if(explodedAssets != null && explodedAssets.size() > 0) {
					List<ExpandedSolutionElement> children = new ArrayList(explodedAssets);
					GenericSort gs = new GenericSort("seID", true);
					Collections.sort(children, gs);
					
					List<String> dupliCateEment=new ArrayList<String>();
					
					for (ExpandedSolutionElement expandedSolutionElement : children) {
						if(!dupliCateEment.contains(expandedSolutionElement.getSeID()))
							explodedAssetData += getDynamicDataForExplodedAsset(expandedSolutionElement.getSeCode(), expandedSolutionElement.getSeID(), expandedSolutionElement.getIpAddress(), expandedSolutionElement.getAlarmId());
						dupliCateEment.add(expandedSolutionElement.getSeID());
					}
					
				} else {
					explodedAssetData += getDynamicDataForExplodedAsset(seCode, seid, null, null);
				}
			
				if(StringUtils.isNotEmpty(explodedAssetData)) {
					dynamicData = dynamicData.replaceFirst("&explodedAssets", explodedAssetData);
				} else {
					dynamicData = dynamicData.replaceFirst("&explodedAssets", " ");
				}
				
				if(StringUtils.isNotEmpty(installScript)) {
					dynamicData = dynamicData.replaceFirst("&installScript", ipModemInstallScript.replaceFirst("&InstallScript", "<pre>" + installScript  + "</pre>"));
				} else {
					dynamicData = dynamicData.replaceFirst("&installScript", " ");
				}
			} catch (Throwable throwable) {
				logger.error("", throwable);
			} finally {
				logger.debug("Exiting getDynamicDataForModem: materialCode" + materialCode + " dialIn:" + dialIn + " seCode:" + seCode + " seid:" + seid + " sid:" + sid + " mid:" + sid + " installScript" + installScript);
			}
			return dynamicData;
		}
	
	public static void main(String [] args) {
		System.out.println(DynamicMailContentRenderer.getDynamicDataForSSLVPN("1083939820", "234123123123123", "Total Service", "12341234", "(000)-000-00000)"));
		System.out.println(DynamicMailContentRenderer.getDynamicDataForSRClosure("1083939820", "IPOLNX", "1-348729008"));
		System.out.println(DynamicMailContentRenderer.getDynamicDataForTobUpdate("1083939820", "IPOLNX", "(000)000-0000", "Change"));
		System.out.println(DynamicMailContentRenderer.getDynamicDataForTobUpdate("1083939820", "IPOLNX", "(000)000-0000", null));
		System.out.println(DynamicMailContentRenderer.getDynamicDataForSALMigration("1083939820", "(000)000-0000+(000)000-1111", "IPOLNX", "(000)000-0000", "Change"));
		System.out.println(DynamicMailContentRenderer.getDynamicDataForSALMigration("1083939820", "(000)000-0000+(000)000-1111", "IPOLNX", "(000)000-0000", null));
		System.out.println(DynamicMailContentRenderer.getDynamicDataForIP("10987659", "123.56.456.90", "T568930948", "23", "IPOLNX", "(000)000-0000", null, null));
		System.out.println(DynamicMailContentRenderer.getDynamicDataForIP("10987659", "123.56.456.90", "T568930948", "23", "IPOLNX", "(000)000-0000", null, "Change"));
		Set<ExpandedSolutionElement> expList = new HashSet<ExpandedSolutionElement>();
		ExpandedSolutionElement expandedSolutionElement1 = new ExpandedSolutionElement();
		expandedSolutionElement1.setSeCode("IP500");
		expandedSolutionElement1.setSeID("(211)111-1111");
		expandedSolutionElement1.setIpAddress(null);
		expandedSolutionElement1.setAlarmId(null);
		ExpandedSolutionElement expandedSolutionElement2 = new ExpandedSolutionElement();
		expandedSolutionElement2.setSeCode("VUS");
		expandedSolutionElement2.setSeID("(122)222-2222");
		expandedSolutionElement2.setIpAddress("12.56.355.40");
		expandedSolutionElement2.setAlarmId(null);
		ExpandedSolutionElement expandedSolutionElement3 = new ExpandedSolutionElement();
		expandedSolutionElement3.setSeCode("AIM");
		expandedSolutionElement3.setSeID("(333)333-3333");
		expandedSolutionElement3.setIpAddress(null);
		expandedSolutionElement3.setAlarmId("1-9900393920");
		ExpandedSolutionElement expandedSolutionElement4 = new ExpandedSolutionElement();
		expandedSolutionElement4.setSeCode("VSPU");
		expandedSolutionElement4.setSeID("(444)444-4444");
		expandedSolutionElement4.setIpAddress("56.890.45.570");
		expandedSolutionElement4.setAlarmId("8906754");
		expList.add(expandedSolutionElement1);
		expList.add(expandedSolutionElement2);
		expList.add(expandedSolutionElement3);
		expList.add(expandedSolutionElement4);
		System.out.println(DynamicMailContentRenderer.getDynamicDataForIP("10987659", "123.56.456.90", "T568930948", "23", "IPOLNX", "(000)000-0000", expList, "Change"));
		System.out.println(DynamicMailContentRenderer.getDynamicDataForModem("10987659", "+1-650-393-5421", "T568930948", "23", "IPOLNX", "(000)000-0000", expList, "Change"));
		System.out.println(DynamicMailContentRenderer.getDynamicDataForSALRecordBuilding("10987659", "(000)000-0000+(888)888-8888", "T568930948", "23", "IPOLNX", "(000)000-0000", expList, "Change"));
		
	}
}
