%% Set the data
clear all;
clc;
%filename = 'Ixx_11_53_9.quv';
%filename = 'Iyy_11_40_7.quv';
%filename = 'Iyy_11_41_15.quv';
%filename = 'Iyy_11_42_22.quv';
%filename = 'Izz_12_4_43.quv';
filename = 'Izz_12_6_27.quv';
data = csvread(filename);

t = data(:,1)-data(1,1);
dt = data(:,2);
roll = data(:,3); 
pitch = data(:,4);
yaw = data(:,5);
acc_x = data(:,6);
acc_y = data(:,7);
acc_z = data(:,8);
quat_0 = data(:,9);
quat_1 = data(:,10);
quat_2 = data(:,11);
quat_3 = data(:,12);

%plot(t,yaw);

%% Inertia Calculations

% based on: Mustapa et al., "Experimental Validation of an Altitud Control for Quadcopter," ARPN Journal of Engineering and Applied Sciences, 2016, p. 3789 - 3795. 
m = 1.568;    % [kg]
g = 9.807;    % [m/s^2]

T_x = 1.168;  % [s]
b_x = 0.173;  % [m]
L_x = 1.18;   % [m]

T_y = 1.080;  % [s]
b_y = 0.173;  % [m]
L_y = 1.10;   % [m]

T_z = 1.122;  % [s]
b_z = 0.265;  % [m]
L_z = 1.025;  % [m]


Ixx = (m*g*(T_x^2)*(b_x^2))/(4*(pi^2)*L_x); % [kg.m^2]
Iyy = (m*g*(T_y^2)*(b_y^2))/(4*(pi^2)*L_y); % [kg.m^2]
Izz = (m*g*(T_z^2)*(b_z^2))/(4*(pi^2)*L_z); % [kg.m^2]


