import React, { useState } from 'react';
import CvUploader from './CvUploader';
import JdForm from './JdForm';
import MatchResults from './MatchResults';

export default function Dashboard() {
  const [resumeId, setResumeId] = useState(null);
  const [jobId, setJobId] = useState(null);

  const handleCvExtracted = (id, skills) => {
    setResumeId(id);
  };

  const handleJdExtracted = (id, skills) => {
    setJobId(id);
  };

  return (
    <div className="min-h-screen bg-slate-50 p-6 md:p-8 lg:p-10 font-sans">
      <header className="mb-8 max-w-7xl mx-auto">
        <h1 className="text-3xl md:text-4xl font-extrabold text-slate-900 tracking-tight">
          DataMatch <span className="text-blue-600">AI</span>
        </h1>
        <p className="text-slate-500 mt-2 font-medium">Intelligent CV & Job Description Alignment System</p>
      </header>

      <main className="max-w-7xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-6 h-auto lg:h-[800px]">
        <CvUploader onCvExtracted={handleCvExtracted} />
        <JdForm onJdExtracted={handleJdExtracted} />
        <MatchResults resumeId={resumeId} jobId={jobId} />
      </main>
    </div>
  );
}
