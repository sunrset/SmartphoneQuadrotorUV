function [desired_Traj, t] = GenerateSimpleTrajectory(waypoints,sample_time,total_time)
%GenerateSimpleTrajectory function
%   Inputs:
%   - total_time 
%   - sample_time
%   - waypoints
    
    t = 0:sample_time:total_time;
    nu = size(t,1);
    
    desired_Traj = repmat(waypoints(:,1),1,round(length(t)/size(waypoints,2))-1);
    desired_Traj = [desired_Traj repmat(waypoints(:,2),1,round(length(t)/size(waypoints,2)))];
    desired_Traj = [desired_Traj repmat(waypoints(:,3),1,round(length(t)/size(waypoints,2))+2)];
    desired_Traj = [desired_Traj repmat(waypoints(:,4),1,round(length(t)/size(waypoints,2)))];
    desired_Traj = [desired_Traj repmat(waypoints(:,5),1,round(length(t)/size(waypoints,2)))];
    desired_Traj = [desired_Traj repmat(waypoints(:,6),1,round(length(t)/size(waypoints,2)))];
    desired_Traj = [desired_Traj repmat(waypoints(:,7),1,round(length(t)/size(waypoints,2)))];
    desired_Traj = [desired_Traj repmat(waypoints(:,8),1,round(length(t)/size(waypoints,2)))];
    desired_Traj = [desired_Traj repmat(waypoints(:,9),1,round(length(t)/size(waypoints,2)))];
    desired_Traj = [desired_Traj repmat(waypoints(:,10),1,round(length(t)/size(waypoints,2)))];
    desired_Traj = [desired_Traj repmat(waypoints(:,11),1,round(length(t)/size(waypoints,2)))];
    desired_Traj = [desired_Traj repmat(waypoints(:,12),1,round(length(t)/size(waypoints,2)))];
    
end

