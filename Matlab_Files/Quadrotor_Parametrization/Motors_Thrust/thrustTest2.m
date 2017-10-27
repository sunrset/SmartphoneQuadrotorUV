clear;
clc;

load('motorsThrustTests.mat');
g = 9.807; % m/s^2

%% Plot Thrust [N] vs V
figure(1);
plot(motor1_test1(:,3), motor1_test1(:,4)*g/1000);
hold on;
plot(motor1_test2(:,3), motor1_test2(:,4)*g/1000);
hold on;
plot(motor1_test3(:,3), motor1_test3(:,4)*g/1000);
hold on;
plot(motor1_test4(:,3), motor1_test4(:,4)*g/1000);
hold on;
plot(motor1_test5(:,3), motor1_test5(:,4)*g/1000);
hold on;
plot(motor2_test1(:,3), motor2_test1(:,4)*g/1000);
hold on;
plot(motor2_test2(:,3), motor2_test2(:,4)*g/1000);
hold on;
plot(motor2_test3(:,3), motor2_test3(:,4)*g/1000);
hold on;
plot(motor2_test4(:,3), motor2_test4(:,4)*g/1000);
hold on;
plot(motor2_test5(:,3), motor2_test5(:,4)*g/1000);
xlabel('Applied Voltage [V]');
ylabel('Thrust [N]');
