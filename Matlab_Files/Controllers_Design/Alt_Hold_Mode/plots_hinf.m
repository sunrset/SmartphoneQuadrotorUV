figure(1);
h = plot(Scope_psi.time, Scope_psi.signals.values(:,1),'g',Scope_psi.time, Scope_psi.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Yaw\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\psi$', '$\psi_{ref}$');
set(lgd,'Interpreter','latex','Location','northwest')
axis([0 30 -0.03 0.23])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_psi_h.eps
%%
figure(2);
h = plot(Scope_theta.time, Scope_theta.signals.values(:,1),'g',Scope_theta.time, Scope_theta.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Roll\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\theta$', '$\theta_{ref}$');
set(lgd,'Interpreter','latex','Location','southeast')
axis([0 30 -0.05 0.24])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_theta_h.eps
%%
figure(3);
h = plot(Scope_phi.time, Scope_phi.signals.values(:,1),'g',Scope_phi.time, Scope_phi.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Pitch\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\phi$', '$\phi_{ref}$');
set(lgd,'Interpreter','latex','Location','southwest')
axis([0 30 -0.22 0.10])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_phi_h.eps
%%
figure(4);
h = plot(Scope_z.time, Scope_z.signals.values(:,1),'g',Scope_z.time, Scope_z.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Altitude$ $[m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$z$', '$z_{ref}$');
set(lgd,'Interpreter','latex','Location','southeast')
axis([0 30 -0.1 1.2])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_z_h.eps
