
(function () {

    angular.getApplicationModule()
            .controller('InvestorCtrl', InvestorCtrl);


    /*
     * InvestorCtrl - controller
     */
    function InvestorCtrl($rootScope, $scope, $log, $http, $location) {
        $log.debug('Main Investor is loaded');
        $scope.userName = 'Admin';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'Main Investor Controller.';

        $scope.data = {};
        $scope.temp = {};
        $scope.data.investor = {};

        if ($rootScope.data !== undefined && $rootScope.data.investor !== undefined) {
            $scope.data.investor = $rootScope.data.investor;
            $rootScope.data.investor = undefined;
        } else {
            $rootScope.data = {};
            $rootScope.data.investor = {};
        }

        $scope.data.accountType = 'INVESTASI';

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
                            $scope.data.investor = response;
                            $rootScope.data.investor.id = response.id;
                            fnUploadNationalId();
                            alert("Account id dengan [" + $scope.data.investor.accountId + "] telah disimpan!");
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
        $scope.temp.file = null;
        $scope.addFile = function (element) {
            var file = element.files[0];
            $scope.temp.fileName = file.name;
            $log.debug(file.name);
        }

        $scope.clearForm = function () {
            $scope.data.investor = {};
            $scope.data.investor.gender = "M";
            $scope.data.investor.province = "jakarta-selatan,-dki";
            $scope.data.investor.nationality = "INDONESIA";
            $scope.data.investor.jobSector = "PROFESSIONAL";
            $scope.data.investor.bankAccount = "BCA";
        }

        function fnUploadNationalId() {
            var fd = new FormData();
            //Take the first selected file
            fd.append("file", $scope.temp.file, $scope.temp.fileName);
            fd.append("nationalId", $scope.data.investor.nationalId);
            
            $log.debug(fd);

            $http.post("/api/investor/storeidentitycopy", fd, {
            withCredentials: true,
                    headers: {'Content-Type':  'application/x-www-form-urlencoded' },
                    transformRequest: angular.identity
            }).success(function(response){
                $log.debug("SUCCESS");
                $log.debug(response);
            }).error(function(err){
                $log.debug("ERROR");
                $log.debug(err);
            });
        }

    }



})();
