%% Set data
    clear;
    clc;
    filename = 'dataAttAcc-2017-12-3-155829.quv';
    data = csvread(filename);
    tfull = (data(:,1)-data(1,1))/3;
    gps_accuracy = data(:,5);
    %data = data(2:14000,:);

    t = (data(:,1)-data(1,1))/1.25;
    dt = data(:,2);
    x = (data(:,3)-data(1,3))*10; 
    y = (data(:,4)-data(1,4))*10;
    %gps_accuracy = data(:,5);
    z = (data(:,6)-data(1,6))*2;
    phi = (data(:,7)-data(1,7))*pi/180;
    theta = (data(:,8)-data(1,8))*pi/180;
    psi = (data(:,9)-data(1,9))*pi/180;
    
%%
figure(1);
h = plot(t, x,'g',t, y,'r',t, z,'b')
set(h,{'LineWidth'},{2;2;2})
ylabel('$Position$ $[m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$x_m$', '$y_m$', '$z_m$');
set(lgd,'Interpreter','latex','Location','northwest')
axis([0 115 -9 7])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 pos_test.eps

%%
figure(2);
h = plot(t, psi,'g',t, theta,'r',t, phi,'b')
set(h,{'LineWidth'},{1;1;1})
ylabel('$Attitude$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\psi_m$', '$\theta_m$', '$\phi_m$');
set(lgd,'Interpreter','latex','Location','northwest')
axis([0 5000 -0.025 0.06])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 att_test.eps

%%
figure(3);
h = plot(tfull, gps_accuracy,'b')
set(h,{'LineWidth'},{2})
ylabel('$GNSS\ Accuracy$ $[m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([31 2100 0 21])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 gpsacc_test.eps