figure(1);
h = plot(Scope_psi.time, Scope_psi.signals.values(:,1),'g',Scope_psi.time, Scope_psi.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Yaw\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\psi$', '$\psi_{ref}$');
set(lgd,'Interpreter','latex','Location','northwest')
axis([0 30 -0.05 0.26])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_psi_lqi.eps

figure(2);
h = plot(Scope_theta.time, Scope_theta.signals.values(:,1),'g',Scope_theta.time, Scope_theta.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Roll\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\theta$', '$\theta_{ref}$');
set(lgd,'Interpreter','latex','Location','northwest')
axis([0 30 -0.12 0.3])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_theta_lqi.eps

figure(3);
h = plot(Scope_phi.time, Scope_phi.signals.values(:,1),'g',Scope_phi.time, Scope_phi.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Pitch\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\phi$', '$\phi_{ref}$');
set(lgd,'Interpreter','latex','Location','northeast')
axis([0 30 -0.31 0.16])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_phi_lqi.eps

figure(4);
h = plot(Scope_z.time, Scope_z.signals.values(:,1),'g',Scope_z.time, Scope_z.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Altitude$ $[m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$z$', '$z_{ref}$');
set(lgd,'Interpreter','latex','Location','northwest')
axis([0 30 -0.1 1.5])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_z_lqi.eps

