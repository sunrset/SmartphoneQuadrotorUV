%% Set the data
clear all;
clc;
filename = 'dataAttAcc-2017-10-15-22040.quv';
data = csvread(filename);

t = data(:,1)-data(1,1);
dt = data(:,2);
roll = data(:,3); 
pitch = data(:,4);
yaw = data(:,5);
acc_x = data(:,6);
acc_y = data(:,7);
acc_z = data(:,8);

plot(t,pitch);
