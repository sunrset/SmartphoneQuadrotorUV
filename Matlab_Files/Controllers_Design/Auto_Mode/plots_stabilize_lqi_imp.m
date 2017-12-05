figure(1);
h = plot(Scope_psi.time, Scope_psi.signals.values(:,1).*2,'g',Scope_psi.time, Scope_psi.signals.values(:,2).*2,'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Yaw\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\psi$', '$\psi_{ref}$');
set(lgd,'Interpreter','latex','Location','northwest')
axis([0 100 -0.2 0.7])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 stabilize_psi_lqi_imp.eps
%%
figure(2);
h = plot(Scope_theta.time, Scope_theta.signals.values(:,1),'g',Scope_theta.time, Scope_theta.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Roll\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\theta$', '$\theta_{ref}$');
set(lgd,'Interpreter','latex','Location','northeast')
axis([0 100 -0.1 0.25])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 stabilize_theta_lqi_imp.eps
%%
figure(3);
h = plot(Scope_phi.time, Scope_phi.signals.values(:,1),'g',Scope_phi.time, Scope_phi.signals.values(:,2),'r')
set(h,{'LineWidth'},{2;2})
ylabel('$Pitch\ angle$ $[rad]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
lgd = legend('$\phi$', '$\phi_{ref}$');
set(lgd,'Interpreter','latex','Location','southeast')
axis([0 100 -0.25 0.1])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 stabilize_phi_lqi_imp.eps

%%
figure(8);
h = plot(Scope_PWM1.time, Scope_PWM1.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('PWM $M_1$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 100 120 190])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 stabilize_pwm1_lqi_imp.eps

figure(9);
h = plot(Scope_PWM2.time, Scope_PWM2.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('PWM $M_2$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 100 120 190])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 stabilize_pwm2_lqi_imp.eps

figure(10);
h = plot(Scope_PWM3.time, Scope_PWM3.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('PWM $M_3$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 100 120 190])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 stabilize_pwm3_lqi_imp.eps

figure(11);
h = plot(Scope_PWM4.time, Scope_PWM4.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('PWM $M_4$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 100 120 190])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 stabilize_pwm4_lqi_imp.eps

%%
figure(12);
h = plot(Scope_u.time, Scope_u.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('$T_u$ $[N]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 100 15 16])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 stabilize_u_lqi_imp.eps
%%
figure(13);
h = plot(Scope_taupsi.time, Scope_taupsi.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('$\tau_\psi$ $[N\cdot m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 100 -0.1 0.3])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 stabilize_taupsi_lqi_imp.eps
%%
figure(14);
h = plot(Scope_tautheta.time, Scope_tautheta.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('$\tau_\theta$ $[N\cdot m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 100 -0.24 0.12])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 stabilize_tautheta_lqi_imp.eps
%%
figure(15);
h = plot(Scope_tauphi.time, Scope_tauphi.signals.values(:,1),'b')
set(h,{'LineWidth'},{1})
ylabel('$\tau_\phi$ $[N\cdot m]$','FontSize',12,'Interpreter','latex');
xlabel('$Time$ $[s]$','FontSize',12,'Interpreter','latex');
grid on
axis([0 100 -0.07 0.25])
a = get(gca,'XTickLabel');
set(gca,'TickLabelInterpreter', 'latex','fontsize',18)
print -depsc2 stabilize_tauphi_lqi_imp.eps
