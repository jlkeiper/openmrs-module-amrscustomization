<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin"><spring:message code="admin.title.short"/></a>
	</li>
	
	<openmrs:hasPrivilege privilege="Upload XSN">
		<li <c:if test='<%= request.getRequestURI().contains("settings") %>'>class="active"</c:if>>
                        <a href="${pageContext.request.contextPath}/module/amrscustomization/settings.htm">
				Settings
			</a>
		</li>
	</openmrs:hasPrivilege>
</ul>
