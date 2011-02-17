<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Edit Patients" otherwise="/login.htm" redirect="/admin/maintenance/mrnGenerator.htm"/>

<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="/WEB-INF/view/admin/maintenance/localHeader.jsp" %>

<script type="text/javascript">
	function toggle(link, id) {
		if (link.innerHTML == '<spring:message code="amrscustomization.MRNGenerator.log.view"/>') {
			link.innerHTML = '<spring:message code="amrscustomization.MRNGenerator.log.hide"/>';
			$j(id).fadeIn();
		}
		else {
			link.innerHTML = '<spring:message code="amrscustomization.MRNGenerator.log.view"/>';
			$j(id).fadeOut();
		}
		return false;
	}
</script>

<style>
	#mrnLog {
		display: none;
	}
</style>

<br />

<h2><spring:message code="amrscustomization.MRNGenerator.title"/></h2>

<br />

<form method="post">
	<table border="0" cellspacing="2" cellpadding="2">
		<tr>
			<td rowspan="4" align="left" valign="top">
				<label for="site"><spring:message code="amrscustomization.MRNGenerator.select.site"/></label><br/>
				<select name="site" size="24">
					<optgroup label="Group A sites">
						<option value="BF">BF - Burnt Forest</option>
						<option value="MO">MO - Mosoriot</option>
						<option value="MT">MT - MTRH Adult</option>
						<option value="MP">MP - MTRH Pediatric</option>
						<option value="TU">TU - Turbo</option>
					</optgroup>
					<optgroup label="Group B sites">
						<option value="AM">AM - Amukura</option>
						<option value="AN">AN - Anderson</option>
                        <option value="AS">AS - Amase</option>
                        <option value="BB">BB - Bumala B</option>
                        <option value="BC">BC - Tambach</option>
                        <option value="BK">BK - Obekae</option>
						<option value="BM">BM - Bumala A</option>
						<option value="BS">BS - Busia</option>
						<option value="CH">CH - Chulaimbo</option>
						<option value="DH">DH - Uasin Gishu District Hospital</option>
				        <option value="DN">DN - AIC Diguna Royal Toto Childrens Home, Ngechek</option>
						<option value="EG">EG - Mt. Elgon Clinic</option>
						<option value="KB">KB - Kabarnet</option>
						<option value="KH">KH - Khuyangu</option>
						<option value="KP">KP - Kapenguria</option>
						<option value="KS">KS - Kibisi</option>
 						<option value="KT">KT - Kitale</option>
						<option value="MB">MB - Moi's Bridge</option>
						<option value="MH">MH - Mukhobola</option>
						<option value="MK">MK - Makutano</option>
						<option value="MU">MU - Moi University</option>
						<option value="NM">NM - Nambale</option>
						<option value="NT">NT - Naitiri</option>
						<option value="PV">PV - Port Victoria</option>
						<option value="SB">SB - Saboti</option>
						<option value="SN">SN - Sango</option>
						<option value="SY">SY - Soy</option>
						<option value="TE">TE - Iten</option>
						<option value="TN">TN - Tenges</option>
						<option value="TS">TS - Teso</option>
						<option value="WB">WB - Webuye</option>
						<option value="ZW">ZW - Ziwa</option>
					</optgroup>
					<optgroup label="Other sites">
						<option value="HC">HC - Highway Clinic</option>
						<option value="PM">PM - PMTCT</option>
						<option value="VC">VC - OVC</option>
					</optgroup>
				</select>
			</td>
			<td align="left" valign="top">
				<label for="first"><spring:message code="amrscustomization.MRNGenerator.starting.number"/></label><br>
				<input name="first" size="10" type="text">
			</td>
		</tr>
		<tr>
			<td align="left" valign="top">
				<label for="prefix"><spring:message code="amrscustomization.MRNGenerator.prefix.number"/></label><br>
				<input type="text" size="4" name="prefix"/>
			</td>
		</tr>
		<tr>
			<td align="left" valign="top">
				<label for="count"><spring:message code="amrscustomization.MRNGenerator.generate.number"/></label><br>
				<select name="count" size="4">
					  <option>100</option>
					  <option>500</option>
					  <option selected="true">1000</option>
					  <option>2000</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				<input type="submit" value="<spring:message code="general.submit"/>">
			</td>
		</tr>
	</table>
</form>

<c:if test="${not empty entries}">

<br/>

<a href="#toggle" onClick="return toggle(this, '#mrnLog')"><spring:message code="MRNGenerator.log.view"/></a>

<br />

<div id="mrnLog">
	<table cellpadding="4" cellspacing="0">
		<tr>
			<th><spring:message code="amrscustomization.MRNGenerator.date"/></th>
			<th><spring:message code="amrscustomization.MRNGenerator.generator"/></th>
			<th><spring:message code="amrscustomization.MRNGenerator.site"/></th>
			<th><spring:message code="amrscustomization.MRNGenerator.first"/></th>
			<th><spring:message code="MRNGenerator.count"/></th>
		</tr>

		<c:forEach items="${entries}" var="entry" varStatus="status">
			<tr class="<c:choose><c:when test="${status.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>">
				<td>${entry.dateGenerated}</td>
				<td>${entry.generatedBy.username}</td>
				<td>${entry.site}</td>
				<td>${entry.first}</td>
				<td>${entry.count}</td>
			</tr>
		</c:forEach>
	</table>
</div>

</c:if>

<%@ include file="/WEB-INF/template/footer.jsp" %>
