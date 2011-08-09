<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="localHeader.jsp"%>

<h2>AMRS Customization Settings</h1>

<p>
        The following settings relate to the AMRS Customization Module.
</p>

<form method="POST">
        <label for="maxUploadSize">Maximum Upload Size</label>
        <input name="maxUploadSize" value="<openmrs:globalProperty key="amrscustomization.maxUploadSize" defaultValue="7500000"/>"/>
        <input type="submit" value="Go"/>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
