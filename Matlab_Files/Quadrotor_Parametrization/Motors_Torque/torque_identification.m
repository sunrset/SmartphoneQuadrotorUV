clear;
clc;
load('motorsTorqueTest.mat')
g = 9.807; % [m/s^2]

%%
figure(1);
plot(motor1_test1(:,2), motor1_test1(:,3));
hold on;
plot(motor1_test2(:,2), motor1_test2(:,3));
hold on;
plot(motor1_test3(:,2), motor1_test3(:,3));
hold on;
plot(motor1_test4(:,2), motor1_test4(:,3));
hold on;
plot(motor1_test5(:,2), motor1_test5(:,3));
hold on;
plot(motor1_test6(:,2), motor1_test6(:,3));
hold on;
plot(motor1_test7(:,2), motor1_test7(:,3));
hold on;
xlabel('PWM [%]');
ylabel('Drag Force [g]');

%%
figure(2);
plot(motor1_test1(:,2)*255/100, motor1_test1(:,3));
hold on;
plot(motor1_test2(:,2)*255/100, motor1_test2(:,3));
hold on;
plot(motor1_test3(:,2)*255/100, motor1_test3(:,3));
hold on;
plot(motor1_test4(:,2)*255/100, motor1_test4(:,3));
hold on;
plot(motor1_test5(:,2)*255/100, motor1_test5(:,3));
hold on;
plot(motor1_test6(:,2)*255/100, motor1_test6(:,3));
hold on;
plot(motor1_test7(:,2)*255/100, motor1_test7(:,3));
hold on;
xlabel('PWM [0, 255]');
ylabel('Drag Force [g]');

%%
figure(3);
plot(motor1_test1(:,4), motor1_test1(:,3).*g*0.244/1000);
hold on;
plot(motor1_test2(:,4), motor1_test2(:,3).*g*0.244/1000);
hold on;
plot(motor1_test3(:,4), motor1_test3(:,3).*g*0.244/1000);
hold on;
plot(motor1_test4(:,4), motor1_test4(:,3).*g*0.244/1000);
hold on;
plot(motor1_test5(:,4), motor1_test5(:,3).*g*0.244/1000);
hold on;
plot(motor1_test6(:,4), motor1_test6(:,3).*g*0.244/1000);
hold on;
plot(motor1_test7(:,4), motor1_test7(:,3).*g*0.244/1000);
hold on;
thrust = linspace(0,5.8);
%drag = 8.7804.*thrust;
drag = 0.0210.*thrust;
line(thrust,drag,'Color','black','LineWidth',1);
text(1,0.1, 'Drag Force [Nm] $$= 0.0210*$$Thrust','Interpreter','latex');
xlabel('Thrust [N]');
ylabel('Drag Force [Nm]');

