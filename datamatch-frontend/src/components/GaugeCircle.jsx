import React from 'react';

export default function GaugeCircle({ score, size = 120, strokeWidth = 12 }) {
  const radius = (size - strokeWidth) / 2;
  const circumference = radius * 2 * Math.PI;
  const offset = circumference - (score / 100) * circumference;

  let strokeColor = 'text-red-500';
  if (score >= 50) strokeColor = 'text-yellow-500';
  if (score >= 80) strokeColor = 'text-green-500';

  return (
    <div className="relative flex items-center justify-center" style={{ width: size, height: size }}>
      <svg width={size} height={size} className="transform -rotate-90">
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          className="text-slate-200"
          strokeWidth={strokeWidth}
          stroke="currentColor"
          fill="transparent"
        />
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          className={`${strokeColor} transition-all duration-1000 ease-in-out`}
          strokeWidth={strokeWidth}
          strokeDasharray={circumference}
          strokeDashoffset={offset}
          strokeLinecap="round"
          stroke="currentColor"
          fill="transparent"
        />
      </svg>
      <div className="absolute flex flex-col items-center justify-center">
        <span className="text-3xl font-bold text-slate-800">{Math.round(score)}%</span>
        <span className="text-xs font-medium text-slate-400 uppercase tracking-wide">Match</span>
      </div>
    </div>
  );
}
