figure(1);
h = plot(Scope_psi.time, Scope_psi.signals.values(:,1).*2,'g',Scope_psi.time, Scope_psi.signals.values(:,2).*2,'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Yaw\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\psi$', '$\psi_{ref}$');
set(lgd,'Interpreter','latex','Location','southwest')
axis([0 40 -0.02 0.02])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_psi_h_imp.eps
%%
figure(2);
h = plot(Scope_theta.time, Scope_theta.signals.values(:,1),'g',Scope_theta.time, Scope_theta.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Roll\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\theta$', '$\theta_{ref}$');
set(lgd,'Interpreter','latex','Location','southeast')
axis([0 70 -0.1 0.1])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_theta_h_imp.eps
%%
figure(3);
h = plot(Scope_phi.time, Scope_phi.signals.values(:,1),'g',Scope_phi.time, Scope_phi.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Pitch\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\phi$', '$\phi_{ref}$');
set(lgd,'Interpreter','latex','Location','southeast')
axis([0 70 -0.1 0.1])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_phi_h_imp.eps
%%
Scope_z.signals.values(:,2) = Scope_z.signals.values(:,2) + 1;
Scope_z.signals.values(:,1) = Scope_z.signals.values(:,1) + 1;
%%
figure(4);
h = plot(Scope_z.time, Scope_z.signals.values(:,1),'g',Scope_z.time, Scope_z.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Altitude$ $[m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$z$', '$z_{ref}$');
set(lgd,'Interpreter','latex','Location','southeast')
axis([0 70 0.5 2.5])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_z_h_imp.eps


%%
figure(8);
h = plot(Scope_PWM1.time, Scope_PWM1.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('PWM $M_1$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 130 180])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_pwm1_h_imp.eps

figure(9);
h = plot(Scope_PWM2.time, Scope_PWM2.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('PWM $M_2$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 140 170])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_pwm2_h_imp.eps

figure(10);
h = plot(Scope_PWM3.time, Scope_PWM3.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('PWM $M_3$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 140 180])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_pwm3_h_imp.eps

figure(11);
h = plot(Scope_PWM4.time, Scope_PWM4.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('PWM $M_4$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 140 170])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_pwm4_h_imp.eps

%%
figure(12);
h = plot(Scope_u.time, Scope_u.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('$T_u$ $[N]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 13.5 17])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_u_h_imp.eps
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
print -depsc2 althold_taupsi_h_imp.eps
%%
figure(14);
h = plot(Scope_tautheta.time, Scope_theta.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('$\tau_\theta$ $[N\cdot m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 -0.12 0.12])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_tautheta_h_imp.eps
%%
figure(15);
h = plot(Scope_tauphi.time, Scope_tauphi.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('$\tau_\phi$ $[N\cdot m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 70 -0.12 0.12])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_tauphi_h_imp.eps
%%
figure(16);
zpos = Scope_z.signals.values(:,1);
zref = Scope_z.signals.values(:,2);
error = abs(zref-zpos);
h = plot(Scope_z.time, error,'b');
set(h,{'LineWidth'},{1})
ylabel('$Pos. Error$ $[m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([4 70 0 1])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 althold_errorpos_h_imp.eps
