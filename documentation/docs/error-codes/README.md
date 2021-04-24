# Error codes
During execution the Machnos Api Gateway can run into an error. These may be shown to the end user, or are visible in the logs. Most of the time an error code is provided with the error message. This section explains these error codes and provides a reason and solution.

Code | Reason | Solution
--- | --- | ---
100000 | General error. This error code is used when an error occurred in code that the Machnos Api Gateway depends on. | 
200000 | Invalid listen interface. | You have configured an unknown interface in the machnos.yml file. Check the machnos.yml file and restart the Machnos Api Gateway.
301000 | Missing variable. | While executing a Function the system is missing a variable. Check your api and execute it again.
301001 | Missing value. | While executing a Function the system is missing a value. Check your api and execute it again.