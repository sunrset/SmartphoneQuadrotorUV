%% Set data
    clear;
    clc;
    filename = 'Tests-10-nov/dataFlightController-2017-11-10-113839.quv';
    data = csvread(filename);

    t = (data(:,1)-data(1,1))./1000;
    dt = data(:,2);
    x = data(:,3); 
    y = data(:,4);
    z = data(:,5);
    theta = data(:,6).*180/pi; 
    phi = data(:,7).*180/pi;
    psi = data(:,8).*180/pi;
    u = data(:,9);
    tau_psi = data(:,10);
    tau_theta = data(:,11);
    tau_phi = data(:,12);
    pwm_m1 = data(:,13);
    pwm_m2 = data(:,14);
    pwm_m3 = data(:,15);
    pwm_m4 = data(:,16);
    quad_battery = data(:,17);
    phone_battery = data(:,18);
    throttle = data(:,19);
    x_ref = data(:,20);
    y_ref = data(:,21);
    z_ref = data(:,22);
    psi_ref = data(:,23).*180/pi;
    theta_ref = data(:,24).*180/pi;
    phi_ref = data(:,25).*180/pi;
    xdot = data(:,26);
    ydot = data(:,27);
    zdot = data(:,28);
    acc_x = data(:,29);
    acc_y = data(:,30);
    acc_z = data(:,31);
    altHkf_z = data(:,32);
    altHkf_zdot = data(:,33);
    altHkf_psi = data(:,34);
    altHkf_psidot = data(:,35);
    altHkf_theta = data(:,36);
    altHkf_thetadot = data(:,37);
    altHkf_phi = data(:,38);
    altHkf_phidot = data(:,39);

%% Plot data
    figure(2);
    subplot(1,3,1);
    plot(t,phi,'g',t,phi_ref,'r');
    leg1 = legend('$\phi$','$\phi_{ref}$');
    set(leg1,'Interpreter','latex');
    title('Pitch ($\phi$) control','Interpreter','latex')
    subplot(1,3,2);
    plot(t,theta,'g',t,theta_ref,'r');
    leg1 = legend('$\theta$','$\theta_{ref}$');
    set(leg1,'Interpreter','latex');
    title('Roll ($\theta$) control','Interpreter','latex')
    subplot(1,3,3);
    plot(t,psi,'g',t,psi_ref,'r');
    leg1 = legend('$\psi$','$\psi_{ref}$');
    set(leg1,'Interpreter','latex');
    title('Yaw ($\psi$) control','Interpreter','latex')
    %%
    figure(3);
    subplot(2,2,1);
    plot(t,pwm_m1);
    title('PWM Motor 1','Interpreter','latex')
    subplot(2,2,2);
    plot(t,pwm_m2);
    title('PWM Motor 2','Interpreter','latex')
    subplot(2,2,3);
    plot(t,pwm_m3);
    title('PWM Motor 3','Interpreter','latex')
    subplot(2,2,4);
    plot(t,pwm_m4);
    title('PWM Motor 4','Interpreter','latex')

%% AltHold KF comparison

    figure(300);
    subplot(1,3,1);
    plot(t,phi,'g',t,altHkf_phi,'r');
    leg1 = legend('$\phi$','$\phi_{altKF}$');
    set(leg1,'Interpreter','latex');
    title('Pitch ($\phi$) comparison','Interpreter','latex')
    subplot(1,3,2);
    plot(t,theta,'g',t,altHkf_theta,'r');
    leg1 = legend('$\theta$','$\theta_{altKF}$');
    set(leg1,'Interpreter','latex');
    title('Roll ($\theta$) comparison','Interpreter','latex')
    subplot(1,3,3);
    plot(t,psi-65,'g',t,-1.*altHkf_psi,'r');
    leg1 = legend('$\psi$','$\psi_{altKF}$');
    set(leg1,'Interpreter','latex');
    title('Yaw ($\psi$) comparison','Interpreter','latex')