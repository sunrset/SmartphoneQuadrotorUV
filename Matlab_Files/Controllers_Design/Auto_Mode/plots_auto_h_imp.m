figure(1);
h = plot(Scope_psi.time, Scope_psi.signals.values(:,1),'g',Scope_psi.time, Scope_psi.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Yaw\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\psi$', '$\psi_{ref}$');
set(lgd,'Interpreter','latex','Location','northwest')
axis([0 40 -0.01 0.01])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_psi_h_imp.eps
%%
figure(2);
h = plot(Scope_theta.time, Scope_theta.signals.values(:,1),'g',Scope_theta.time, Scope_theta.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Roll\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\theta$', '$\theta_{ref}$');
set(lgd,'Interpreter','latex','Location','southeast')
axis([0 70 -0.15 0.15])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_theta_h_imp.eps
%%
figure(3);
h = plot(Scope_phi.time, Scope_phi.signals.values(:,1),'g',Scope_phi.time, Scope_phi.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Pitch\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\phi$', '$\phi_{ref}$');
set(lgd,'Interpreter','latex','Location','southeast')
axis([0 70 -0.15 0.15])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_phi_h_imp.eps
%%
Scope_z.signals.values(:,2) = Scope_z.signals.values(:,2) + 2
Scope_z.signals.values(:,1) = Scope_z.signals.values(:,1) + 2
figure(4);
h = plot(Scope_z.time, Scope_z.signals.values(:,1),'g',Scope_z.time, Scope_z.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Altitude$ $[m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$z$', '$z_{ref}$');
set(lgd,'Interpreter','latex','Location','northeast')
axis([0 70 1.5 2.5])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_z_h_imp.eps
%%
figure(5);
h = plot(Scope_x.time, Scope_x.signals.values(:,1),'g',Scope_x.time, Scope_x.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Position\ x$ $[m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$x$', '$x_{ref}$');
set(lgd,'Interpreter','latex','Location','northwest')
axis([0 70 -0.2 2.2])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_x_h_imp.eps
%%
figure(6);
h = plot(Scope_y.time, Scope_y.signals.values(:,1),'g',Scope_y.time, Scope_y.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Position\ y$ $[m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$y$', '$y_{ref}$');
set(lgd,'Interpreter','latex','Location','northwest')
axis([0 70 -0.2 2.2])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_y_h_imp.eps

%%
figure(7);
%h = plot(Scope_y.time, Scope_y.signals.values(:,1),'g',Scope_y.time, Scope_y.signals.values(:,2),'r')
h = plot3(Scope_x.signals.values(500:10000,1),Scope_y.signals.values(500:10000,1),Scope_z.signals.values(500:10000,1),'g',Scope_x.signals.values(500:10000,2),Scope_y.signals.values(500:10000,2),Scope_z.signals.values(500:10000,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Position\ y$ $[m]$','FontSize',12,'Interpreter','latex');
xlabel('$Position\ x$ $[m]$','FontSize',12,'Interpreter','latex');
zlabel('$Position\ z$ $[m]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$Quadrotor\ position$', '$Position\ reference$');
set(lgd,'Interpreter','latex','Location','northeast')
axis([-0.2 2.2 -0.2 2.2 1.3 2.7])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_xyz_h_imp.eps

%%
figure(8);
h = plot(Scope_PWM1.time, Scope_PWM1.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('PWM $M_1$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 140 170])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_pwm1_h_imp.eps

figure(9);
h = plot(Scope_PWM2.time, Scope_PWM2.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('PWM $M_2$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 140 170])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_pwm2_h_imp.eps

figure(10);
h = plot(Scope_PWM3.time, Scope_PWM3.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('PWM $M_3$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 140 170])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_pwm3_h_imp.eps

figure(11);
h = plot(Scope_PWM4.time, Scope_PWM4.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('PWM $M_4$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 140 170])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_pwm4_h_imp.eps

%%
figure(12);
h = plot(Scope_u.time, Scope_u.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('$T_u$ $[N]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 14.5 16])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_u_h_imp.eps
%%
figure(13);
h = plot(Scope_taupsi.time, Scope_taupsi.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('$\tau_\psi$ $[N\cdot m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 -0.01 0.01])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_taupsi_h_imp.eps
%%
figure(14);
h = plot(Scope_tautheta.time, Scope_theta.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('$\tau_\theta$ $[N\cdot m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 -0.15 0.15])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_tautheta_h_imp.eps
%%
figure(15);
h = plot(Scope_tauphi.time, Scope_tauphi.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('$\tau_\phi$ $[N\cdot m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 -0.1 0.1])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_tauphi_h_imp.eps

%%
figure(16);
xpos = Scope_x.signals.values(:,1);
ypos = Scope_y.signals.values(:,1);
zpos = Scope_z.signals.values(:,1);
xref = Scope_x.signals.values(:,2);
yref = Scope_y.signals.values(:,2);
zref = Scope_z.signals.values(:,2);
error = (((xref-xpos).^2)+((yref-ypos).^2)+((zref-zpos).^2)).^(0.5);
h = plot(Scope_x.time, error,'b')
set(h,{'LineWidth'},{1})
ylabel('$Pos. Error$ $[m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([4 70 0 0.4])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_errorpos_h_imp.eps

