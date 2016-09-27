(function () {
    'use strict';
    angular.getApplicationModule()
            .factory('ErrorHandler', ErrorHandler);
    function ErrorHandler($log, $state) {

        return {
            handlingHttp: HttpErrorHandler
        };

        function HttpErrorHandler(error, callback) {
            try {
                var data = JSON.parse(error.data.message);
                if (data.code && data.code === 500 && data.message === "Invalid token.") {
                    $state.go('login');
                }
            } catch (e) {
                $log.debug("handleError: e.stackTrace()");
                $log.debug(e);
            }
            callback;
        }

    }
})();