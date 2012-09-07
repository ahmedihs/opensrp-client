function ECList(ecListBridge) {
    return {
        populateInto: function (cssIdentifierOfRootElement) {
            $(cssIdentifierOfRootElement).html(Handlebars.templates.ec_list(ecListBridge.getECs()));
        },
        bindEveryItemToECView: function (cssIdentifierOfRootElement, cssIdentifierOfEveryListItem) {
            $(cssIdentifierOfRootElement).on("click", cssIdentifierOfEveryListItem, function (event) {
                ecListBridge.delegateToECDetail($(this).data("caseid"));
            });
        },
        bindItemToCommCare: function(cssIdentifierOfElement) {
            $(cssIdentifierOfElement).click(function () {
                ecListBridge.delegateToCommCare($(this).data("form"));
            })
        }
    };
}

function ECListBridge() {
    var ecContext = window.context;
    if (typeof ecContext === "undefined" && typeof FakeECListContext !== "undefined") {
        ecContext = new FakeECListContext();
    }

    return {
        getECs: function () {
            var ec = JSON.parse(ecContext.get());
            return {"ec": ec};
        },
        delegateToECDetail: function (caseId) {
            return ecContext.startEC(caseId);
        },
        delegateToCommCare: function (formId) {
            ecContext.startCommCare(formId);
        }
    };
}

function FakeECListContext() {
    return {
        get: function() {
            return JSON.stringify({
                highPriority: [
                    {
                        caseId: "12345",
                        wifeName: "Wife 1",
                        husbandName: "Husband 1",
                        ecNumber: "EC Number 1",
                        villageName: "Village 1",
                        isHighPriority: true,
                        hasTodos: false
                    }
                ],
                normalPriority: [
                    {
                        caseId: "11111",
                        wifeName: "Wife 2",
                        husbandName: "Husband 2",
                        ecNumber: "EC Number 2",
                        villageName: "Village 2",
                        isHighPriority: false,
                        hasTodos: true

                    }
                ]
            });
        },
        startEC: function(caseId) {
            window.location.href = "ec_detail.html";
        },
        startCommCare: function (formId) {
            alert("Start CommCare with form " + formId);
        }
    }
}