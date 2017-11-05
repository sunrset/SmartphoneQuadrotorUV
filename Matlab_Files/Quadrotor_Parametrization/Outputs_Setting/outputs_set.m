
Kd = 0.0210;
L = 0.244; % [m]
d = L*cos(pi/4);

% U = [u, tau_psi, tau_theta, tau_phi]';
% F = [F_1, F_2, F_3, F_4]';
% U = M*F;

M = [1    1   1    1;
     Kd  -Kd  Kd  -Kd;
     -d  -d   d    d;
     -d   d   d   -d];
 
% F = M_inv*U;
M_inv = inv(M);
     

% Thrust to PWM [0, 255] conversion: y = -1.983x^{2} + 47.84x + 3.835
