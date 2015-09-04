<html>
<head>
    <meta name="layout" content="actuator" charset="utf-8" />
</head>

<body>
    <tmpl:page title="Services">
        <table id="serviceTable" class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>ID</th>
                <th>URL</th>
            </tr>
            <g:each in="${services}" var="service">
                <tr>
                    <td><g:link controller="service" action="show" id="${service.id}">${service.id}</g:link></td>
                    <td>${service.host}:${service.port}</td>
                </tr>
            </g:each>

            </thead>
        </table>
    </tmpl:page>
</body>
</html>