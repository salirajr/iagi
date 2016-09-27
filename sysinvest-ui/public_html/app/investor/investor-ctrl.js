
(function () {

    angular.getApplicationModule()
            .controller('InvestorCtrl', InvestorCtrl);

    /*
     * InvestorCtrl - controller
     */
    function InvestorCtrl($rootScope, $scope, $log, $http, $location, ErrorHandler) {

        function initiate() {
            $scope.lookup = {};
            $scope.data = {};
            $scope.temp = {};
            $scope.data.investor = {};
            $scope.data.accountType = 'INVESTASI';


            $http.get("/api/lookup/ret/findByGroupName?value=COUNTRY")
                    .then(function (response) {
                        $log.debug(response);
                        $scope.lookup.countries = response.data;
                    }, ErrorHandler.handlingHttp);

            $http.get("/api/lookup/ret/findByGroupName?value=PROVINCE")
                    .then(function (response) {
                        $log.debug(response);
                        $scope.lookup.provinces = response.data;
                    }, ErrorHandler.handlingHttp);

            $http.get("/api/lookup/ret/findByGroupName?value=JOBSECTOR")
                    .then(function (response) {
                        $log.debug(response);
                        $scope.lookup.jobsectors = response.data;
                    }, ErrorHandler.handlingHttp);

            $http.get("/api/lookup/ret/findByGroupName?value=BANK")
                    .then(function (response) {
                        $log.debug(response);
                        $scope.lookup.banks = response.data;
                    }, ErrorHandler.handlingHttp);

            if ($rootScope.data !== undefined && $rootScope.data.investor !== undefined) {
                $scope.data.investor = $rootScope.data.investor;
                $rootScope.data.investor = undefined;
            } else {
                $rootScope.data = {};
                $rootScope.data.investor = {};
            }
        }
        initiate();




        $scope.saveInvestor = function () {
            var isConfirm = confirm("Anda akan menyimpan data investor berikut, apakah anda yakin?");
            if (!isConfirm)
                return;

            if ($scope.data.accountType === 'INVESTASI') {
                var payload = {};
                angular.copy($scope.data.investor, payload);
                payload.birthDate = new Date(payload.birthDate);
                $http.post('/api/investor/addnew', payload)
                        .success(function (response) {
                            $log.debug(response);
                            $scope.data.investor = response;
                            $rootScope.data.investor.id = response.id;
                            fnUploadNationalId();
                            alert("Account id dengan [" + $scope.data.investor.accountId + "] telah disimpan!");
                            $scope.getAccount();
                        }).error(function (err) {
                    alert("Account id dengan [" + $scope.data.investor.accountId + "] gagal tersimpan!");
                    $log.debug(err);
                });

            } else {

            }
        };

        $scope.nextToCovenant = function () {
            var isConfirm = false;
            if ($scope.data.investor.id === undefined) {
                isConfirm = confirm("Anda melanjutkan proses investasi tanpa menyimpan data investor berikut, apakah anda yakin?");
            } else {
                isConfirm = confirm("Anda akan melanjutkan proses investasi dengan data investor berikut, apakah anda yakin?");
            }

            if (!isConfirm)
                return;
            $rootScope.data.investor = $scope.data.investor;
            $location.path('/index/covenant');
        };

        $scope.getAccount = function () {
            var tAccountId = $scope.data.investor.accountId;
            $http.get("/api/investor/ret/byaccountid?value=" + $scope.data.investor.accountId)
                    .then(function (response) {
                        $log.debug(response);
                        if (response.data !== "") {
                            $scope.data.investor = response.data;
                            $log.debug("getAccount responsed sucess with " + $scope.data.investor.accountId);
                        } else {
                            alert("Account id dengan [" + $scope.data.investor.accountId + "] tidak ditemukan!");
                            $scope.clearForm();
                            $scope.data.investor.accountId = tAccountId;
                        }

                    });

        };

        $scope.temp = {};
        $scope.addFile = function (element) {
            $scope.temp.file = element.files[0];
            $scope.temp.fileName = element.files[0].name;
            $log.debug($scope.temp.fileName);
        };

        $scope.clearForm = function () {
            $scope.data.investor = {};
            $scope.data.investor.gender = "M";
            $scope.data.investor.province = "jakarta-selatan,-dki";
            $scope.data.investor.nationality = "INDONESIA";
            $scope.data.investor.jobSector = "PROFESSIONAL";
            $scope.data.investor.bankAccount = "BCA";
            $scope.data.investor.birthDate = "1981-01-01";
            $scope.temp = {};
        };

        function fnUploadNationalId() {
            console.log($scope.temp.file);
            if ($scope.temp.file !== undefined) {
                var fd = new FormData();
                var fileName = $scope.data.investor.nationalId + "." + $scope.temp.fileName.split('.').pop();
                //Take the first selected file
                fd.append("file", $scope.temp.file, $scope.temp.fileName);
                fd.append("nationalId", fileName);
                fd.append("id", $scope.data.investor.id);

                $http.post("/api/investor/storeidentitycopy", fd, {
                    withCredentials: true,
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }
        }

    }



})();
