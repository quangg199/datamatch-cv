import React, { useState } from 'react';
import apiClient from '../api/client';
import EmptyState from './EmptyState';
import GaugeCircle from './GaugeCircle';
import { Loader2, Zap } from 'lucide-react';

export default function MatchResults({ resumeId, jobId }) {
  const [matchResult, setMatchResult] = useState(null);
  const [isMatching, setIsMatching] = useState(false);
  const [errorMsg, setErrorMsg] = useState(null);

  const handleAutoMatch = async () => {
    if (!resumeId || !jobId) return;
    setIsMatching(true);
    setErrorMsg(null);

    try {
      const res = await apiClient.post('/matching/auto', {
        resumeId,
        jobDescriptionId: jobId
      });
      setMatchResult(res.data);
    } catch (err) {
      console.error(err);
      setErrorMsg('Matching failed. Please ensure both CV and JD skills are fully extracted.');
    } finally {
      setIsMatching(false);
    }
  };

  const isReady = resumeId && jobId;

  return (
    <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 h-full flex flex-col">
      <h2 className="text-xl font-bold text-slate-800 mb-6 flex items-center">
        <Zap className="w-5 h-5 text-yellow-500 mr-2" />
        3. Matching Analytics
      </h2>

      {errorMsg && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg">
          {errorMsg}
        </div>
      )}

      {!isReady && !matchResult && (
        <div className="flex-1 flex items-center justify-center">
          <EmptyState message="Extract skills for both CV and JD to enable matching." />
        </div>
      )}

      {isReady && !matchResult && (
        <div className="flex-1 flex flex-col items-center justify-center p-8">
          <div className="w-full text-center">
            <h3 className="text-lg font-medium text-slate-800 mb-2">Ready for Analysis</h3>
            <p className="text-sm text-slate-500 mb-8">
              Both Candidate Profile and Job Description are processed. Run the AI matching engine to see the alignment.
            </p>
            <button
              onClick={handleAutoMatch}
              disabled={isMatching}
              className="w-full flex justify-center items-center py-4 px-6 border border-transparent rounded-xl shadow-lg text-base font-bold text-white bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 transform hover:-translate-y-0.5 transition-all focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              {isMatching ? (
                <><Loader2 className="w-5 h-5 mr-3 animate-spin" /> Analyzing Synergy...</>
              ) : (
                'Execute AI Auto-Match'
              )}
            </button>
          </div>
        </div>
      )}

      {matchResult && (
        <div className="flex-1 flex flex-col overflow-y-auto min-h-0">
          <div className="flex justify-center mb-8 pt-4">
            <GaugeCircle score={matchResult.score} size={160} strokeWidth={14} />
          </div>

          <div className="grid grid-cols-2 gap-4 flex-1">
            <div className="bg-green-50/50 p-4 rounded-xl border border-green-100">
              <h4 className="text-xs font-bold text-green-800 uppercase tracking-wider mb-3">
                Matched Skills ({matchResult.matchedSkills.length})
              </h4>
              <ul className="space-y-2">
                {matchResult.matchedSkills.map((skill, idx) => (
                  <li key={idx} className="flex items-start text-sm text-green-700">
                    <span className="mr-2">•</span> {skill}
                  </li>
                ))}
                {matchResult.matchedSkills.length === 0 && (
                  <li className="text-sm text-green-600/50 italic">None matched</li>
                )}
              </ul>
            </div>

            <div className="bg-red-50/50 p-4 rounded-xl border border-red-100">
              <h4 className="text-xs font-bold text-red-800 uppercase tracking-wider mb-3">
                Missing Skills ({matchResult.missingSkills.length})
              </h4>
              <ul className="space-y-2">
                {matchResult.missingSkills.map((skill, idx) => (
                  <li key={idx} className="flex items-start text-sm text-red-700">
                    <span className="mr-2">•</span> {skill}
                  </li>
                ))}
                {matchResult.missingSkills.length === 0 && (
                  <li className="text-sm text-red-600/50 italic">No missing skills</li>
                )}
              </ul>
            </div>
          </div>
          
          <div className="mt-6 pt-4 border-t border-slate-100">
             <button
              onClick={handleAutoMatch}
              disabled={isMatching}
              className="w-full flex justify-center items-center py-2 px-4 border border-slate-200 rounded-lg text-sm font-medium text-slate-700 bg-white hover:bg-slate-50 transition-colors"
            >
              {isMatching ? <Loader2 className="w-4 h-4 mr-2 animate-spin" /> : 'Re-run Analysis'}
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
