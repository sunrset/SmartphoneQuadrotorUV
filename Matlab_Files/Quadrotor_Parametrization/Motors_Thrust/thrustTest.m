clear;
clc;

load('motorsThrustTest.mat');
g = 9.807; % m/s^2

%% Plot Thrust [N] vs PWM [0, 255]
figure(1);
plot(motor1_test1(:,2)*255/100, motor1_test1(:,3)*g/1000);
hold on;
plot(motor1_test2(:,2)*255/100, motor1_test2(:,3)*g/1000);
hold on;
plot(motor1_test3(:,2)*255/100, motor1_test3(:,3)*g/1000);
hold on;
plot(motor1_test4(:,2)*255/100, motor1_test4(:,3)*g/1000);
hold on;
plot(motor1_test5(:,2)*255/100, motor1_test5(:,3)*g/1000);
hold on;
plot(motor2_test1(:,2)*255/100, motor2_test1(:,3)*g/1000);
hold on;
plot(motor2_test2(:,2)*255/100, motor2_test2(:,3)*g/1000);
hold on;
plot(motor2_test3(:,2)*255/100, motor2_test3(:,3)*g/1000);
hold on;
plot(motor2_test4(:,2)*255/100, motor2_test4(:,3)*g/1000);
hold on;
plot(motor2_test5(:,2)*255/100, motor2_test5(:,3)*g/1000);
hold on;
% Trendline:
% Thrust [N] = 5.441e-5*(X^2) + 0.01586X + 0.014808,  with X -> PWM 0..255
pwm = linspace(0,255);
thrust = 5.441e-5.*(pwm.*pwm) + 0.01586.*pwm + 0.014808;
line(pwm,thrust,'Color','black','LineWidth',2);
text(30,6, '$$y = 5.441\times10^{-5}x^{2} + 0.01586x + 0.014808$$','Interpreter','latex');
xlabel('PWM [0, 255]');
ylabel('Thrust [N]');

%% Plot  PWM [0, 255] vs Thrust [N]
figure(2);
plot(motor1_test1(:,3)*g/1000, motor1_test1(:,2)*255/100);
hold on;
plot(motor1_test2(:,3)*g/1000, motor1_test2(:,2)*255/100);
hold on;
plot(motor1_test3(:,3)*g/1000, motor1_test3(:,2)*255/100);
hold on;
plot(motor1_test4(:,3)*g/1000, motor1_test4(:,2)*255/100);
hold on;
plot(motor1_test5(:,3)*g/1000, motor1_test5(:,2)*255/100);
hold on;
plot(motor2_test1(:,3)*g/1000, motor2_test1(:,2)*255/100);
hold on;
plot(motor2_test2(:,3)*g/1000, motor2_test2(:,2)*255/100);
hold on;
plot(motor2_test3(:,3)*g/1000, motor2_test3(:,2)*255/100);
hold on;
plot(motor2_test4(:,3)*g/1000, motor2_test4(:,2)*255/100);
hold on;
plot(motor2_test5(:,3)*g/1000, motor2_test5(:,2)*255/100);
hold on;
% Trendline:
% Thrust [PWM 0..255] = -1.983(X^2) + 47.84X + 3.835,  with X -> Thrust [N]
thrust = linspace(0,8);
%pwm = -1.983.*(thrust.*thrust) + 47.84.*thrust + 3.835;
pwm = -1.783.*(thrust.*thrust) + 47.84.*thrust + 0;
line(thrust,pwm,'Color','black','LineWidth',2);
text(0.7,200, '$$y = -1.983x^{2} + 47.84x + 3.835$$','Interpreter','latex');
xlabel('Thrust [N]');
ylabel('PWM [0, 255]');


%% Trendline
% Thrust [g] = 0.03607(X^2) + 4.12X +1.5108,  with X being % from 0 to 100
% Thrust [N] = 5.441e-5*(X^2) + 0.01586X + 0.014808, with X being [PWM 0..255] 
% Thrust [PWM 0..255] = -1.983(X^2) + 47.84X + 3.835,  with X -> Thrust [N]