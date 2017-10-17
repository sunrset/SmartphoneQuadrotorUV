%% Quad-rotor Model
%   
%  Universidad del Valle - Research Group of Industrial Control
%  Alejandro Astudillo Vigoya, 08.12.2016
%
%  updated: 17.10.2017


%% Quad-rotor Model

function G = Quadrotor_model4()

            % Gravity constant
              g = 9.807;

              m = 1.192;
              Ixx = 0.0135;
              Iyy = 0.0124;
              Izz = 0.0336;

            % Number of states:
            nx = 12;

            % Number of inputs:
            nu = 4;

            % Number of outputs:
            ny = 4;

            x_Init         =  2         ;
            x_dot_Init     =  0           ;
            y_Init         =  2          ;
            y_dot_Init     =  0           ;
            z_Init         =  1.5           ;
            z_dot_Init     =  0           ;
            psi_Init       =  45 * pi/180 ;
            psi_dot_Init   =  0           ;
            theta_Init     =  0 * pi/180 ;
            theta_dot_Init =  0           ;
            phi_Init       =  0 * pi/180 ;
            phi_dot_Init   =  0           ;

            X0 = [x_Init          ;
                  x_dot_Init      ;
                  y_Init          ;
                  y_dot_Init      ;
                  z_Init          ;
                  z_dot_Init      ;
                  psi_Init        ;
                  psi_dot_Init    ;
                  theta_Init      ;
                  theta_dot_Init  ;
                  phi_Init        ;
                  phi_dot_Init   ];

        % Nonlinear Model

            %Inputs = U, States
            %Outputs = States_dot  
            %States: X dX Y dY Z dZ Psi dPsi Theta dTheta Phi dPhi
            %        1  2 3  4 5  6  7   8     9      10   11  12
            
            States = X0;
            U = [0,0,0,0];

            dX = States(2);
            ddX = ((-U(1)/m)*sin(States(9))) - g*sin(States(9));
            dY = States(4);
            ddY = ((U(1)/m)*cos(States(9))*sin(States(11))) + g*cos(States(9))*sin(States(11));
            dZ = States(6);
            ddZ = ((U(1)/m)*cos(States(9))*cos(States(11))) + (g*cos(States(9))*cos(States(11))) - g;
            dPsi = States(8);
            ddPsi = (((Ixx-Iyy)/Izz)*States(12)*States(10)) + (U(2)/Izz);
            dTheta = States(10);
            ddTheta = (((Izz-Ixx)/Iyy)*States(12)*States(8)) + (U(3)/Iyy);
            dPhi = States(12);
            ddPhi = (((Iyy-Izz)/Ixx)*States(10)*States(8)) + (U(4)/Ixx);

            States_dot = [dX, ddX, dY, ddY, dZ, ddZ, dPsi, ddPsi, dTheta, ddTheta, dPhi, ddPhi]';

        % Linearized Model

            A = [  0  1  0  0  0  0  0  0  0  0  0  0   ;
                   0  0  0  0  0  0  0  0  g  0  0  0   ;
                   0  0  0  1  0  0  0  0  0  0  0  0   ;
                   0  0  0  0  0  0  0  0  0  0  g  0   ;
                   0  0  0  0  0  1  0  0  0  0  0  0   ;
                   0  0  0  0  0  0  0  0  0  0  0  0   ;
                   0  0  0  0  0  0  0  1  0  0  0  0   ;
                   0  0  0  0  0  0  0  0  0  0  0  0   ;
                   0  0  0  0  0  0  0  0  0  1  0  0   ;
                   0  0  0  0  0  0  0  0  0  0  0  0   ;
                   0  0  0  0  0  0  0  0  0  0  0  1   ;
                   0  0  0  0  0  0  0  0  0  0  0  0 ] ;
            
            A = A-(1e-3)*eye(12); %To move the eigenvalues from 0 to -0.001

            B = [  0  0  0  0  0 1/m  0  0     0  0      0    0;
                   0  0  0  0  0  0   0 1/Izz  0  0      0    0;
                   0  0  0  0  0  0   0  0     0  1/Iyy  0    0;
                   0  0  0  0  0  0   0  0     0  0      0    1/Ixx ]';

            %C = eye(ny,nx);
            
            C = [1 0 0 0 0 0 0 0 0 0 0 0;
                 0 0 1 0 0 0 0 0 0 0 0 0;
                 0 0 0 0 1 0 0 0 0 0 0 0;
                 0 0 0 0 0 0 1 0 0 0 0 0];

            DD = zeros(ny,nu);

            states = {'X' 'dX' 'Y' 'dY' 'Z' 'dZ'  'Psi' 'dPsi' 'Theta' 'dTheta' 'Phi' 'dPhi' };
            inputs = {'u-mg' 'tao_psi' 'tao_theta' 'tao_phi'};
            outputs = {'X' 'Y' 'Z' 'Psi'};

            Gss = ss(A,B,C,DD,'statename',states,'inputname',inputs,'outputname',outputs);

            %Gss=ss(A,B,C,DD);
            Gssmin=ss(Gss,'min');
            %G=Gssmin;
            G = Gss;

end