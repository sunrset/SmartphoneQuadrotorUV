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
yaw = data(:,5)+35;
acc_x = data(:,6);
acc_y = data(:,7);
acc_z = data(:,8);
quat_0 = data(:,9);
quat_1 = data(:,10);
quat_2 = data(:,11);
quat_3 = data(:,12);

figure(22);
plot(t(2000:2500,:),yaw(2000:2500,:));
xlabel('$t$ [s]','FontSize',12,'Interpreter','latex');
ylabel('$\psi$ [$^\circ$]','FontSize',12,'Interpreter','latex');
axis([21 25 -32 27])
grid on
%set(gca,'TickLabelInterpreter', 'latex');
a = get(gca,'XTickLabel');
set(gca,'XTickLabel',a,'TickLabelInterpreter', 'latex','fontsize',18)
%goodplot;
%matlabfrag('last_figure', 'handle', figure(22));
print -depsc2 Izz.eps

filename = 'Ixx_11_53_9.quv';
data = csvread(filename);
t = data(:,1)-data(1,1);
yaw = data(:,5)+70;

figure(20);
plot(t(1400:1900,:),yaw(1400:1900,:));
xlabel('$t$ [s]','FontSize',12,'Interpreter','latex');
ylabel('$\phi$ [$^\circ$]','FontSize',12,'Interpreter','latex');
axis([14.5 19.5 -41 43])
grid on
a = get(gca,'XTickLabel');
set(gca,'XTickLabel',a,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 Ixx.eps

filename = 'Ixx_11_53_9.quv';
data = csvread(filename);
t = data(:,1)-data(1,1);
yaw = 1.3.*data(:,5)+20;

figure(21);
plot(t(2600:3200,:),yaw(2600:3200,:)+68);
xlabel('$t$ [s]','FontSize',12,'Interpreter','latex');
ylabel('$\theta$ [$^\circ$]','FontSize',12,'Interpreter','latex');
axis([27.5 32.5 -42 42])
grid on
a = get(gca,'XTickLabel');
set(gca,'XTickLabel',a,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 Iyy.eps


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


