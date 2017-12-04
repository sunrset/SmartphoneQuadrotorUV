figure(1);
h = plot(Scope_psi.time, Scope_psi.signals.values(:,1),'g',Scope_psi.time, Scope_psi.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Yaw\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\psi$', '$\psi_{ref}$');
set(lgd,'Interpreter','latex','Location','northwest')
axis([0 40 -0.03 0.07])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_psi_h.eps
%%
figure(2);
h = plot(Scope_theta.time, Scope_theta.signals.values(:,1),'g',Scope_theta.time, Scope_theta.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Roll\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\theta$', '$\theta_{ref}$');
set(lgd,'Interpreter','latex','Location','southeast')
axis([0 70 -0.06 0.04])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_theta_h.eps
%%
figure(3);
h = plot(Scope_phi.time, Scope_phi.signals.values(:,1),'g',Scope_phi.time, Scope_phi.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Pitch\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\phi$', '$\phi_{ref}$');
set(lgd,'Interpreter','latex','Location','southeast')
axis([0 70 -0.04 0.03])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_phi_h.eps
%%
figure(4);
h = plot(Scope_z.time, Scope_z.signals.values(:,1),'g',Scope_z.time, Scope_z.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Altitude$ $[m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$z$', '$z_{ref}$');
set(lgd,'Interpreter','latex','Location','northeast')
axis([0 70 -0.01 0.06])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_z_h.eps
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
print -depsc2 auto_x_h.eps
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
print -depsc2 auto_y_h.eps

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
axis([-0.2 2.2 -0.2 2.2 -0.2 0.2])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 auto_xyz_h.eps