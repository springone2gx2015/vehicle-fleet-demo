<html>
<head>
    <meta name="layout" content="actuator" charset="utf-8" />

</head>

<body>
<tmpl:page title="Service Environment: ${service.id} (${service.uri})">
    <h4 class="managedTitle">Managed Properties</h4> 

    <form id="managedForm">
        Set Property:
        <input class="key" name="key"></input>
        <input class="value" name="value"></input>
        <input class="submit" type="submit"></input>
    </form>  
    <table id="managedTable" class="table table-bordered table-hover">
        <tr>
            <td>Key</td>
            <td>Value</td>
        </tr>
    <g:findAll in="${service.env}" expr="it.key == 'manager'" var="propertySource">
        <g:if test="${propertySource.value}">


                <g:each var="setting" in="${propertySource.value}">
                    <tr>
                        <td>${setting.key}</td>
                        <td>${setting.value}</td>
                    </tr>
                </g:each>
                                                                   

        </g:if>
    </g:findAll>
    </table><br/>

    <g:findAll in="${service.env}" expr="it.key != 'manager'" var="propertySource">
        <g:if test="${propertySource.value}">
            <h4>PropertySource: ${propertySource.key}</h4>
            <table id="serviceTable" class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>Key</th>
                    <th>Value</th>
                    <g:if test="${propertySource.value instanceof Map}">
                        <g:each in="${propertySource.value}" var="setting">
                            <tr>
                                <td>${setting.key}</td>
                                <td>
                                    <span id="${setting.key}" class="settingValue">${setting.value}</span>
                                </td>
                            </tr>
                        </g:each>
                    </g:if>
                </tr>
                </thead>
            </table>
            <br />
            <br/>
        </g:if>        
    </g:findAll>
</tmpl:page>
<g:javascript>

    $(document).ready(function() {
        $('#managedForm').submit(function(event) {
            var key = this.key.value
            var val = this.value.value
            $.ajax({
                type:"POST",
                data: key + "=" + val,
                url: "${createLink(uri:service.uri)}"
            }).done(function( d ) {
                $('#managedTable tr:last').after(
                    '<tr><td>' + key + '</td><td>'+ val +'</td></tr>')    
                console.log( "Sample of data:", JSON.stringify(d) );
            });        
            return false
        });
    });
</g:javascript>
</body>
</html>