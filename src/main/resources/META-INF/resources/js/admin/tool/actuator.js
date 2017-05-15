jQuery(document).ready(function () {
    function draw(target, used, total, scala, unit) {
        target.css("width", 100 * used / total + "%");
        target.children("span").text((used / scala).toFixed(2) + " " + unit + " / " + (total / scala).toFixed(2) + " " + unit);
    }

    function drawDisk(data) {
        let total = data.diskSpace.total;
        let free = data.diskSpace.free;
        let used = total - free;
        draw(jQuery("#disk-progress-bar"), used, total, Math.pow(1000, 3), "GB");
    }

    function drawMemory(data) {
        let total = data["mem"];
        let free = data["mem.free"];
        let used = total - free;
        draw(jQuery("#memory-progress-bar"), used, total, 1024, "MiB");
    }

    function drawInstanceInfo(data) {
        jQuery("#contain-time").append(data["uptime"] / 1000 + " s");
        jQuery("#instance-time").append(data["instance.uptime"] / 1000 + " s");
        jQuery("#process-number").append(data["processors"]);
        jQuery("#system-load").append(data["systemload.average"]);
    }

    function drawDatasourceType(data) {
        jQuery("#datasource-type").append(data.db.database);
    }

    function drawDatasourceConnection(data) {
        jQuery("#active-datasource-connection").append(data["datasource.primary.active"]);
        jQuery("#datasource-connection-pool-usage").append(data["datasource.primary.usage"]);
    }

    function drawHttpSessions(data) {
        jQuery("#max-http-sessions").append(data["httpsessions.max"]);
        jQuery("#active-http-sessions").append(data["httpsessions.active"]);
    }

    function loadActuatorData() {
        //health
        jQuery.get(endpointURL + "health", function (data) {
            drawDisk(data);
            drawDatasourceType(data);
        });

        //metrics
        jQuery.get(endpointURL + "metrics", function (data) {
            drawMemory(data);
            drawInstanceInfo(data);
            drawDatasourceConnection(data);
            drawHttpSessions(data);
        });
    }

    loadActuatorData();
});
