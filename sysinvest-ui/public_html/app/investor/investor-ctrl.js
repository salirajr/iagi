
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

        $rootScope.data = {};
        $rootScope.data.investor = {};

        $scope.data.accountType = 'INVESTASI';

        $scope.saveInvestor = function () {
            var isConfirm = confirm("Anda akan menyimpan data investor berikut, apakah anda yakin?");
            if (!isConfirm)
                return;

            if ($scope.data.accountType === 'INVESTASI') {
                var payload = $scope.data.investor;
                payload.birthDate = new Date($scope.data.investor.birthDate);
                $http.post('/api/investor/addnew', payload)
                        .success(function (response) {
                            $scope.data.investor = response;
                            $rootScope.data.investor.id = response.id;
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
            $http.get("/api/investor/ret/byaccountid?value=" + $scope.data.investor.accountId)
                    .then(function (response) {
                        $log.debug(response);
                        if (response.data !== "") {
                            $scope.data.investor = response.data;
                            $log.debug("getAccount responsed sucess with " + $scope.data.investor.accountId);
                        } else {
                            alert("Account id dengan [" + $scope.data.investor.accountId + "] tidak ditemukan!");
                             $scope.clearForm();
                        }

                    });

        };

        $scope.addFile = function (element) {
            var files = element.files;
            $scope.temp.fileName = files[0].name;
            $log.debug(files[0].name);
        }

        $scope.clearForm = function () {
            $scope.data.investor = {};
            $scope.data.investor.gender = "M";
            $scope.data.investor.province="jakarta-selatan,-dki";
            $scope.data.investor.nationality="INDONESIA";
            $scope.data.investor.jobSector="PROFESSIONAL";
            $scope.data.investor.bankAccount = "BCA";
        }

    }



})();
