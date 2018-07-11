# Idcard register progress

- Enable bluetooth if closed

- Connect device and read IdCard
  - Obtain mac address and ensure device bound
    - Obtain from cache
    - Obtain form bound devices if no cache
    - Obtain by scan device and bind device if no bound
  - Obtain device by mac address
  - Connect device

- reread IdCard on read failed

- Check Registered on read success
  - Do register on unregistered
    - Bind password and register
    - Register face feature on register success
    - Upload profile to Qiniu on register face success
    - Upload profile url to server on upload profile to Qiniu success
    - Success!!! Router to home page
  - Do login on registered

