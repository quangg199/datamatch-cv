import React, { useState } from 'react';
import apiClient from '../api/client';
import { Loader2, CheckCircle } from 'lucide-react';

export default function JdForm({ onJdExtracted }) {
  const [formData, setFormData] = useState({ title: '', companyName: '', rawDescription: '' });
  const [jobId, setJobId] = useState(null);
  const [status, setStatus] = useState('IDLE'); // IDLE, SAVING, SAVED, EXTRACTING, EXTRACTED, ERROR
  const [skills, setSkills] = useState([]);
  const [errorMsg, setErrorMsg] = useState(null);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setStatus('IDLE');
    setSkills([]);
  };

  const handleProcessJd = async () => {
    if (!formData.title || !formData.companyName || !formData.rawDescription) {
      setErrorMsg('Please fill in all fields.');
      return;
    }
    
    setStatus('SAVING');
    setErrorMsg(null);
    let currentJobId = jobId;

    try {
      // Step 1: Save JD if not saved yet for this edit
      if (status === 'IDLE' || !currentJobId) {
        const res = await apiClient.post('/jobs', formData);
        currentJobId = res.data.id;
        setJobId(currentJobId);
      }
      
      setStatus('EXTRACTING');
      
      // Step 2: Extract skills
      const extractRes = await apiClient.post(`/jobs/${currentJobId}/extract-skills`);
      setSkills(extractRes.data.requiredSkills || []);
      setStatus('EXTRACTED');
      onJdExtracted(currentJobId, extractRes.data.requiredSkills || []);

    } catch (err) {
      console.error(err);
      setStatus('ERROR');
      setErrorMsg('Failed to process Job Description. Please check connection.');
    }
  };

  return (
    <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 h-full flex flex-col">
      <h2 className="text-xl font-bold text-slate-800 mb-6">2. Job Description</h2>

      {errorMsg && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg">
          {errorMsg}
        </div>
      )}

      <div className="space-y-4 mb-6">
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Job Title</label>
          <input
            type="text"
            name="title"
            value={formData.title}
            onChange={handleChange}
            placeholder="e.g. Senior Frontend Engineer"
            className="w-full px-4 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all text-sm"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Company Name</label>
          <input
            type="text"
            name="companyName"
            value={formData.companyName}
            onChange={handleChange}
            placeholder="e.g. TechCorp"
            className="w-full px-4 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all text-sm"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Raw Description</label>
          <textarea
            name="rawDescription"
            value={formData.rawDescription}
            onChange={handleChange}
            rows="6"
            placeholder="Paste the full job description here..."
            className="w-full px-4 py-2 border border-slate-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all text-sm resize-none"
          />
        </div>
      </div>

      <div className="mt-auto pt-4 border-t border-slate-100">
        <button
          onClick={handleProcessJd}
          disabled={['SAVING', 'EXTRACTING'].includes(status)}
          className={`w-full flex justify-center items-center py-3 px-4 border border-transparent rounded-xl shadow-sm text-sm font-medium text-white ${
            ['SAVING', 'EXTRACTING'].includes(status) ? 'bg-indigo-400 cursor-not-allowed' : 'bg-indigo-600 hover:bg-indigo-700'
          } transition-colors`}
        >
          {['SAVING', 'EXTRACTING'].includes(status) ? (
            <><Loader2 className="w-4 h-4 mr-2 animate-spin" /> Processing with AI...</>
          ) : (
            'Save & Extract JD Skills'
          )}
        </button>
      </div>

      {status === 'EXTRACTED' && (
        <div className="mt-6 flex-1 overflow-y-auto min-h-0">
          <div className="flex items-center justify-between mb-3">
            <h3 className="text-sm font-semibold text-slate-800 uppercase tracking-wider">Required Skills</h3>
            <CheckCircle className="w-4 h-4 text-green-500" />
          </div>
          <div className="flex flex-wrap gap-2">
            {skills.map((skill, idx) => (
              <span key={idx} className="px-3 py-1 bg-indigo-50 text-indigo-700 text-xs font-medium rounded-full border border-indigo-100">
                {skill}
              </span>
            ))}
            {skills.length === 0 && <span className="text-sm text-slate-500 italic">No skills extracted.</span>}
          </div>
        </div>
      )}
      {/* Popup thông báo AI đang chạy */}
      {status === 'EXTRACTING' && (
        <div className="fixed bottom-6 right-6 bg-white border-l-4 border-indigo-500 shadow-2xl rounded-xl p-4 flex items-center space-x-4 z-50">
          <Loader2 className="w-6 h-6 text-indigo-500 animate-spin" />
          <div>
            <p className="text-sm font-bold text-slate-800">Đang trích xuất AI...</p>
            <p className="text-xs text-slate-600">AI đang trích xuất dữ liệu yêu cầu công việc, vui lòng chờ...</p>
          </div>
        </div>
      )}
    </div>
  );
}
