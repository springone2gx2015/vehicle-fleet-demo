<html>
<head>
    <meta name="layout" content="actuator" charset="utf-8" />

</head>

<body>
<a href="#" id="username" data-type="text" data-pk="1" data-url="/post" data-title="Enter username">superuser</a>
<tmpl:page title="Service Environment: ${service.id} (${service.uri})">
    <g:each in="${service.env}" var="propertySource">
        <g:if test="${propertySource.value}">
            <h4>PropertySource: ${propertySource.key}</h4>
            <table id="serviceTable" class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>Key</th>
                    <th>Value</th>
                    <g:each in="${propertySource.value}" var="setting">
                        <tr>
                            <td>${setting.key}</td>
                            <td>
                                <span id="${setting.key}" class="settingValue">${setting.value}</span>
                            </td>
                        </tr>
                    </g:each>
                </tr>
                </thead>
            </table>
            <br />
            <br/>
        </g:if>
    </g:each>
</tmpl:page>
<g:javascript>
    $(document).ready(function() {
        $('.settingValue').editable(function(value, settings) {
            $.ajax({
                processData:false,
                data: this.id + '=' + value,
                type:"POST",
                url: "${createLink(uri: service.uri)}",
            }).done(function( d ) {
                console.log( "Sample of data:", JSON.stringify(d) );
            });
            return value
        });
    });
</g:javascript>
</body>
</html>