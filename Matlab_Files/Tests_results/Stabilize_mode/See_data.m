%% Set data
    clear;
    clc;
    filename = 'test1FlightController-2017-11-5-0-45-4.quv';
    data = csvread(filename);

    t = (data(:,1)-data(1,1))./1000;
    dt = data(:,2);
    x = data(:,3); 
    y = data(:,4);
    z = data(:,5);
    theta = data(:,6); 
    phi = data(:,7);
    psi = data(:,8);
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
    psi_ref = data(:,23);
    theta_ref = data(:,24);
    phi_ref = data(:,25);

    altHkf_z = data(:,26);
    altHkf_zdot = data(:,27);
    altHkf_psi = data(:,28);
    altHkf_psidot = data(:,29);
    altHkf_theta = data(:,30);
    altHkf_thetadot = data(:,31);
    altHkf_phi = data(:,32);
    altHkf_phidot = data(:,33);

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
    plot(t,phi,'g',t,altHkf_phi.*(-pi/180),'r');
    leg1 = legend('$\phi$','$\phi_{altKF}$');
    set(leg1,'Interpreter','latex');
    title('Pitch ($\phi$) comparison','Interpreter','latex')
    subplot(1,3,2);
    plot(t,theta,'g',t,altHkf_theta.*(-pi/180),'r');
    leg1 = legend('$\theta$','$\theta_{altKF}$');
    set(leg1,'Interpreter','latex');
    title('Roll ($\theta$) comparison','Interpreter','latex')
    subplot(1,3,3);
    plot(t,psi,'g',t,altHkf_psi.*(-pi/180),'r');
    leg1 = legend('$\psi$','$\psi_{altKF}$');
    set(leg1,'Interpreter','latex');
    title('Yaw ($\psi$) comparison','Interpreter','latex')